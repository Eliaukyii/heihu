package com.example.business.service.processor.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.*;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.other.Inventory;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.response.HeihuAuthResponse;
import com.example.business.service.processor.Processor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * 物料清单
 */
@Component
@Slf4j
public class B_MaterialListImpl implements Processor {

    @Autowired
    private ObjectMapper objectMapperUpper;

    @Autowired
    private ObjectMapper objectMapper;
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

        //根据code查询工艺路线列表
        B_ProcessData processData = new B_ProcessData();
        if (dataErp.getRouting() != null && dataErp.getRouting().get("Code") != null) {

            String routingCode = dataErp.getRouting().get("Code").toString();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("code", routingCode);

            HeihuAuthResponse response = webClientErp.post()
                    .uri(apiParamsHeihu.processUri)
                    .bodyValue(paramMap)
                    .retrieve()
                    .bodyToMono(HeihuAuthResponse.class)
                    .block();
            if (!response.getCode().equals("200")) {
                log.error("工艺路线获取失败，code：{}，失败信息：" + response.getMessage(), routingCode);
                return;
            }
            try {
                processData = objectMapper.readValue(response.getData().toString(), B_ProcessData.class);
            } catch (Exception e) {
                log.error("工艺路线反序列化失败，失败信息：" + e.getMessage());
                return;
            }
            if (CollectionUtils.isEmpty(processData.getProcesses())) {
                log.error("工艺路线下工序信息为空，工艺路线code：{}", routingCode);
                return;
            }

        }

        B_DataHeihu data = this.disposeData(dataErp, processData);
        log.info("物料清单，订阅审核消息，即将在黑湖新增的数据：" + data);

        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        HeihuAuthResponse heihuResponse = webClient.post()
                .uri(apiParamsHeihu.bomUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block();

        if (!heihuResponse.getCode().equals("200")) {
            log.error("新增物料清单失败，失败信息：" + heihuResponse.getMessage());
        }

    }

    private B_DataHeihu disposeData(B_DataErp erp, B_ProcessData processData) {
        Integer seq = 10;
        Integer lineSeq = 1;

        List<B_Processes> processes = processData.getProcesses();
        boolean flagProcess = erp.getRouting() != null && erp.getRouting().get("Code") != null;
        String processNumMin = null;

        B_DataHeihu heihu = new B_DataHeihu();
        heihu.setMaterialCode(erp.getCode());
        heihu.setProductRate(erp.getYieldRate());

        if (flagProcess) {
            heihu.setProcessRoute(erp.getRouting().get("Code").toString());

            String processNumMax = processes.stream()
                    .max(Comparator.comparingInt(B_Processes::getProcessSeq))
                    .map(B_Processes::getProcessNum).orElse(null);
            processNumMin = processes.stream()
                    .min(Comparator.comparingInt(B_Processes::getProcessSeq))
                    .map(B_Processes::getProcessNum).orElse(null);

            heihu.setWorkReportProcessNum(processNumMax);

        }

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
            heihuChild.setSeq(seq);
            seq += 10;
            heihuChild.setMaterialCode(erpChild.getInventory().getCode());
            heihuChild.setInputAmountNumerator(erpChild.getProduceQuantity());
            heihuChild.setInputAmountDenominator(erpChild.getRequiredQuantity());
            heihuChild.setLossRate(erpChild.getWasteRate());
            heihuChild.setPickMode("1");  //按需领料
            heihuChild.setSpecificProcessInput(1);

            heihuChild.setInputProcessNum(processNumMin);

            ArrayList<B_BomFeedingControlsHeihu> bomFeedingControlsHeihuList = new ArrayList<>();
            B_BomFeedingControlsHeihu bomFeedingControlsHeihu = new B_BomFeedingControlsHeihu(lineSeq++, 0, 1);
            bomFeedingControlsHeihuList.add(bomFeedingControlsHeihu);
            heihuChild.setBomFeedingControls(bomFeedingControlsHeihuList);

            heihuChildList.add(heihuChild);
        }

        heihu.setBomInputMaterials(heihuChildList);
        heihu.setStatus(1);

        return heihu;
    }

    @Override
    public String getType() {
        return MsgType.BOM_AUDIT;
    }
}
