package com.example.business.service.processor.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.*;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.other.Inventory;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.service.processor.Processor;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.extern.slf4j.Slf4j;
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

    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：物料清单");
        Inventory inventory = msgInfo.getBizContent().getInventory();
        if (inventory == null || inventory.getCode() == null) {
            log.error("物料清单相关数据有误，未找到物料code");
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
        dtoMap.put("param", requestMap);

        B_DataErp erpResponseData = webClientErp.post()
                .uri(apiParamsErp.bomUri)
                .bodyValue(requestMap)
                .retrieve()
                .bodyToMono(B_DataErp.class)
                .block();

        //根据Name查询工艺路线列表
        String name = erpResponseData.getRouting().get("Name").toString(); //工艺路线名
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> requestMap2 = new HashMap<>();
        requestMap2.put("Name", name);
        dtoMap.put("param", requestMap2);

        List<Map<String, Object>> list = new ArrayList<>();  //存储工艺路线列表
        list = webClientErp.post()
                .uri(apiParamsErp.routingUri)
                .bodyValue(paramMap)
                .retrieve()
                .bodyToMono(list.getClass())
                .block();

        B_DataHeihu data = this.disposeData(erpResponseData, list);
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
        log.info("物料清单，订阅审核消息，在黑湖新增 - 请求黑湖响应数据：" + heihuResponse);


    }

    private B_DataHeihu disposeData(B_DataErp erp, List<Map<String, Object>> list) {
        Integer seq = 10;
        Integer lineSeq = 1;

        B_DataHeihu heihu = new B_DataHeihu();
        heihu.setMaterialCode(erp.getCode());
        heihu.setProductRate(erp.getYieldRate());
        heihu.setProcessRoute(list.get(list.size()-1).get("Code").toString());
        //todo workReportProcessNum待定
        heihu.setReportingMethods(new Integer[]{5, 6});
        heihu.setVersion(erp.getVersion());
        heihu.setDefaultVersion(erp.getIsCostBOM());
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
            heihuChild.setInputProcessNum(list.get(0).get("Code").toString());  //工艺路线首道序Code
            B_BomFeedingControlsHeihu bomFeedingControlsHeihu = new B_BomFeedingControlsHeihu(lineSeq++, 0, 1);
            heihuChild.setBomFeedingControls(bomFeedingControlsHeihu);

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
