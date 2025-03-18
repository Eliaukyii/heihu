package com.example.business.service.processor.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.*;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.other.Inventory;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.service.processor.Processor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料清单
 */
@Component
@Slf4j
public class B_MaterialListImpl implements Processor {

    @Autowired
    private ObjectMapper objectMapperUpper;
    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：物料清单");
        Inventory inventory = msgInfo.getBizContent().getInventory();
        if (inventory == null || inventory.getCode() == null) {
            log.error("物料清单相关数据有误，未找到父物料code");
            return;
        }
        String code = inventory.getCode();

        //请求erp获取详细信息
        WebClient webClientErp = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("openToken", SaveToken.erpToken)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //请求erp的物料清单数据
        Map<String, Object> dtoMap = new HashMap<>();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("Code", code);
        dtoMap.put("dto", requestMap);

        String responseData = webClientErp.post()
                .uri(apiParamsErp.bomUri)
                .bodyValue(dtoMap)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<B_DataErp> listDataErp = new ArrayList<>();
        try {
            listDataErp = objectMapperUpper.readValue(responseData, new TypeReference<List<B_DataErp>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("物料清单 - 数据转换失败");
            return;
        }

        if (CollectionUtils.isEmpty(listDataErp)) {
            log.error("未查询到相应父物料编号'{}'下的物料清单", code);
            return;
        }

        B_DataErp dataErp = listDataErp.get(listDataErp.size() - 1);

        //根据Name查询工艺路线列表
        List<Map<String, Object>> routingList = null;
        if (dataErp.getRouting() != null) {
            String name = dataErp.getRouting().get("Name").toString(); //工艺路线名
            Map<String, Object> paramMap = new HashMap<>();
            Map<String, Object> requestMap2 = new HashMap<>();
            requestMap2.put("Name", name);
            paramMap.put("param", requestMap2);

            routingList = webClientErp.post()
                    .uri(apiParamsErp.routingUri)
                    .bodyValue(paramMap)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    })
                    .block();

        }

        B_DataHeihu data = this.disposeData(dataErp, routingList);
        log.info("物料清单，订阅审核消息，即将在黑湖新增的数据：" + data);

        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String heihuResponse = webClient.post()
                .uri(apiParamsHeihu.bomUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //todo 操作失败写入日志
        log.info("物料清单，订阅审核消息，在黑湖新增 - 请求黑湖响应数据：" + heihuResponse);

    }

    private B_DataHeihu disposeData(B_DataErp erp, List<Map<String, Object>> routingList) {
        Integer seq = 10;
        Integer lineSeq = 1;

        B_DataHeihu heihu = new B_DataHeihu();
        heihu.setMaterialCode(erp.getCode());
        heihu.setProductRate(erp.getYieldRate());
        if (CollectionUtils.isNotEmpty(routingList)) {
            heihu.setProcessRoute(routingList.get(routingList.size()-1).get("Code").toString());
        }

        //todo workReportProcessNum待定
        heihu.setReportingMethods(new Integer[]{5, 6});
        heihu.setVersion(erp.getVersion());
        Integer isCostBOM = 0;
        if (erp.getIsCostBOM() != null) {
            isCostBOM = erp.getIsCostBOM() ? 1 : 0;
        }
        heihu.setDefaultVersion(isCostBOM);
        heihu.setWarehousing(1);
        heihu.setAutoWarehousingFlag(1);

        //组装子物料
        List<B_DataChildErp> erpChildList = erp.getBOMChildDTOs();
        List<B_DataChildHeihu> heihuChildList = new ArrayList<>(erpChildList.size());
        for (B_DataChildErp erpChild : erpChildList) {
            B_DataChildHeihu heihuChild = new B_DataChildHeihu();
            heihuChild.setSeq(seq); seq += 10;
            heihuChild.setMaterialCode(erpChild.getInventory().getCode());
            heihuChild.setInputAmountNumerator(erpChild.getProduceQuantity());
            heihuChild.setInputAmountDenominator(erpChild.getRequiredQuantity());
            heihuChild.setLossRate(erpChild.getWasteRate());
            heihuChild.setPickMode("1");  //按需领料
            heihuChild.setSpecificProcessInput(1);

            if (CollectionUtils.isNotEmpty(routingList)) {
                //工艺路线首道序Code
                JSONArray routingDetails = JSONUtil.parseArray(routingList.get(routingList.size() - 1).get("RoutingDetails"));
                String process = JSONUtil.parseObj(routingDetails.get(0)).get("Process").toString();
                String code = JSONUtil.parseObj(process).get("Code").toString();
                heihuChild.setInputProcessNum(code);
            }

            ArrayList<B_BomFeedingControlsHeihu> bomFeedingControlsHeihuList = new ArrayList<>();
            B_BomFeedingControlsHeihu bomFeedingControlsHeihu = new B_BomFeedingControlsHeihu(lineSeq++, 0, 1);
            bomFeedingControlsHeihuList.add(bomFeedingControlsHeihu);
            heihuChild.setBomFeedingControls(bomFeedingControlsHeihuList);

            heihuChildList.add(heihuChild);
        }

        heihu.setBomInputMaterials(heihuChildList);

        return heihu;
    }

    @Override
    public String getType() {
        return MsgType.BOM_AUDIT;
    }
}
