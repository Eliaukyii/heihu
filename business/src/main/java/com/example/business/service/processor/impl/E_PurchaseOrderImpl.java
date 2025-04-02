package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.PurchaseOrderHeihu;
import com.example.business.domain.PurchaseOrderOther.*;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.msg.MsgInfoBizContent;
import com.example.business.domain.msgPurchaseOrder.MsgInfoPurchaseOrder;
import com.example.business.domain.msgPurchaseOrder.MsgInfoPurchaseOrderData;
import com.example.business.domain.msgPurchaseOrder.PurchaseOrderDetails;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.request.RequestParamErpPurchase;
import com.example.business.domain.response.HeihuAuthResponse;
import com.example.business.service.processor.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

/**
 * 采购订单
 */
@Component
@Slf4j
public class E_PurchaseOrderImpl implements Processor {

    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：采购订单");

        //获取单据编号
        MsgInfoBizContent bizContent = msgInfo.getBizContent();
        String voucherCode = bizContent.getVoucherCode();
        log.info("bizContent值:" + bizContent);
        log.info("voucherCode值:" + voucherCode);

        //查询erp的采购订单详情
        WebClient webClientErp = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("openToken", SaveToken.erpToken)
                .defaultHeader("appKey",apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        log.info("webClientErp:" + webClientErp);
        //RequestParamErp requestParamErp = new RequestParamErp(voucherCode);
        RequestParamErpPurchase requestParamErpPurchase = new RequestParamErpPurchase(voucherCode);
        log.info("requestParamErp:" + requestParamErpPurchase);

        MsgInfoPurchaseOrder msgInfoPurchaseOrder = webClientErp.post()
                .uri(apiParamsErp.purchaseOrderUri)
                .bodyValue(requestParamErpPurchase)
                .retrieve()
                .bodyToMono(MsgInfoPurchaseOrder.class)
                .block();
        log.info(",采购订单新增 - 根据voucherCode请求erp的响应数据: " + msgInfoPurchaseOrder);
        MsgInfoPurchaseOrderData msgInfoPurchaseOrderData = msgInfoPurchaseOrder.getData();

        //组装数据
        //MsgInfoPurchaseOrderData data1 = msgInfoPurchaseOrder1.getData();
        MsgInfoPurchaseOrderData data1 = msgInfoPurchaseOrderData;
        PurchaseOrderHeihu data = convertRequest(data1);
        log.info("data:" + data);
        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HeihuAuthResponse heihuResponse = webClient.post()
                .uri(apiParamsHeihu.purchaseOrderUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block();
        if (!heihuResponse.getCode().equals("200")) {
            log.error("采购订单，订单新增 - 请求黑湖响应数据：" + heihuResponse.getData() +"；" + heihuResponse.getMessage());
        } else {
            HashMap<String, String[]> map = new HashMap<>();
            map.put("codes", new String[]{data.getCode()});
            //请求黑湖
            WebClient webClient2 = WebClient.builder()
                    .baseUrl(apiParamsHeihu.url)
                    .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
            HeihuAuthResponse heiHuResponse2 = webClient2.post()
                    .uri(apiParamsHeihu.purchaseOrderIssueUri)
                    .bodyValue(map)
                    .retrieve()
                    .bodyToMono(HeihuAuthResponse.class)
                    .block();
            log.error("采购订单下发成功：" + heiHuResponse2.getData() +"；" + heiHuResponse2.getMessage());
            if (!heiHuResponse2.getCode().equals("200")) {
                log.error("采购订单下发失败：" + heiHuResponse2.getData() +"；" + heiHuResponse2.getMessage());
            }

        }

    }

    private PurchaseOrderHeihu convertRequest(MsgInfoPurchaseOrderData data){
        PurchaseOrderHeihu purchaseOrderHeihu = new PurchaseOrderHeihu();
        purchaseOrderHeihu.setCode(data.getCode());
        purchaseOrderHeihu.setSupplierCode(data.getPartner().getCode());
        purchaseOrderHeihu.setDefaultInStorageType(2);
        purchaseOrderHeihu.setOwnerCode("admin");
        purchaseOrderHeihu.setSource(0);

        List<CustomField> customFields = new ArrayList<>();
        customFields.add(new CustomField("cust_field2__c",data.getID()));
        purchaseOrderHeihu.setCustomFields(customFields);

        purchaseOrderHeihu.setUpperNoteType(new upperNoteType("001"));
        purchaseOrderHeihu.setMaterialCarryMode(0);
        purchaseOrderHeihu.setDeliveryMode(0);

        List<PurchaseOrderDetails> details = data.getPurchaseOrderDetails();
        purchaseOrderHeihu.setUnitName(details.get(0).getUnit().getName());

        List<itemList> itemLists = details.stream().map(detail -> {
            itemList itemList1 = new itemList();
            itemList1.setDemandAmount(detail.getQuantity());
            itemList1.setDemandTime(detail.getAcceptDate());
            itemList1.setMaterialCode(detail.getInventory().getCode());
            //itemList1.setMaterialCode("MA00000000");
            itemList1.setLineNo("10");
            List<CustomFields2> customFields2 =itemList1.getCustomFields();
            for (CustomFields2 cf : customFields2){
                FieldValue2 fieldValue2 = cf.getFieldValue();
                cf.setFieldValue(fieldValue2);
                fieldValue2.setCustField2c(data.getID().toString());
                break;
            }

            return itemList1;
        }).collect(Collectors.toList());
        purchaseOrderHeihu.setItemList(itemLists);
        return purchaseOrderHeihu;
    }


    @Override
    public String getType() {
        return MsgType.PURCHASEORDER_AUDIT;
    }
}
