package com.example.business.service.processor.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.SaleOrderHeihu;
import com.example.business.domain.SaleOrderOther.*;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrder;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrderData;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.request.RequestParamErpPurchase;
import com.example.business.domain.response.HeihuAuthResponse;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售订单
 */
@Component
@Slf4j
public class D_SaleorderImpl implements Processor {

    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：销售订单");
        //获取单据编号
        String voucherCode = msgInfo.getBizContent().getVoucherCode();

        //查询erp的销售订单详细信息
        WebClient webClientErp = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("openToken", SaveToken.erpToken)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //RequestParamErp requestParamErp = new RequestParamErp(voucherCode);
        RequestParamErpPurchase requestParamErpPurchase = new RequestParamErpPurchase(voucherCode);

        MsgInfoSaleOrder msgInfoSaleOrder1 = webClientErp.post()
                .uri(apiParamsErp.saleOrderUri)
                .bodyValue(requestParamErpPurchase)
                .retrieve()
                .bodyToMono(MsgInfoSaleOrder.class)
                .block();
        log.info("销售订单,销售订单新增 - 根据voucherCode请求erp的响应数据: " + msgInfoSaleOrder1);

        //组装数据
        MsgInfoSaleOrderData data1 = msgInfoSaleOrder1.getData();
        SaleOrderHeihu data = covertRequest(data1);

        JSON parse = JSONUtil.parse(data);
        log.info("parse数据：" + parse);
        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HeihuAuthResponse heiHuResponse = webClient.post()
                .uri(apiParamsHeihu.saleOrderUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block();
        if (!heiHuResponse.getCode().equals("200")) {
            log.error("销售订单,销售订单新增 - 请求黑湖响应数据：" + heiHuResponse.getData() +"；" + heiHuResponse.getMessage());
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
                    .uri(apiParamsHeihu.saleOrderIssueUri)
                    .bodyValue(map)
                    .retrieve()
                    .bodyToMono(HeihuAuthResponse.class)
                    .block();
            log.error("销售订单下发成功：" + heiHuResponse2.getData() +"；" + heiHuResponse2.getMessage());
            if (!heiHuResponse2.getCode().equals("200")) {
                log.error("销售订单下发失败：" + heiHuResponse2.getData() +"；" + heiHuResponse2.getMessage());
            }

        }
    }

    private SaleOrderHeihu covertRequest(MsgInfoSaleOrderData data) {
        SaleOrderHeihu saleOrderHeihu = new SaleOrderHeihu();
        saleOrderHeihu.setCode(data.getCode());
        saleOrderHeihu.setCustomerCode(data.getCustomer().getCode());
        saleOrderHeihu.setOutboundType("直接出库");
//        saleOrderHeihu.setOwnerCode(data.getAuditor());
        saleOrderHeihu.setOwnerCode("admin");
        saleOrderHeihu.setReceiveInformation(data.getAddress());
        saleOrderHeihu.setContactName(data.getLinkMan());
        String phoneNumber = data.getCustomerPhone();
        phoneNumber = phoneNumber.replaceAll("[\\s-]+", "");
        saleOrderHeihu.setPhoneNumber(phoneNumber);
        saleOrderHeihu.setRemark(data.getMemo());
        //saleOrderHeihu.setPhoneNumber(data.getCustomerPhone());
        List<SaleOrderDetails> details = data.getSaleOrderDetails();
        List<items> itemsList = details.stream().map(detail -> {
            items items1 = new items();
            items1.setLineNo(detail.getID());
            items1.setMaterialCode(detail.getInventory().getCode());
            items1.setAmount(detail.getQuantity());
            items1.setUnit(detail.getUnit().getName());
            items1.setDeliveryDate(detail.getDeliveryDate());
//            items1.setRemark(data.getMemo());

/*            FieldValue fieldValue = new FieldValue();
            fieldValue.setCustField2C(data.getID().toString());*/

            CustomFields customFields = new CustomFields();
            customFields.setFieldCode("cust_field2__c");
            customFields.setFieldValue(detail.getID().toString());

            if (items1.getCustomFields() == null){
                items1.setCustomFields(new ArrayList<>());
            }
            items1.getCustomFields().add(customFields);
/*            List<CustomFields> customFields2 = items1.getCustomFields();
            for (CustomFields cf : customFields2){
                FieldValue fieldValue = cf.getFieldValue();
                cf.setFieldValue(fieldValue);
                fieldValue.setCustField2C(data.getID().toString());
                break;
            }*/

            return items1;
        }).collect(Collectors.toList());
        saleOrderHeihu.setItems(itemsList);


        List<String> dynamicPropertyKeys = data.getDynamicPropertyKeys();
        //包装要求
        int pubuserdefnvc1Index = dynamicPropertyKeys.indexOf("pubuserdefnvc1");
        String pubuserdefnvc1 = data.getDynamicPropertyValues().get(pubuserdefnvc1Index);

        int pubuserdefnvc2Index = dynamicPropertyKeys.indexOf("pubuserdefnvc2");
        String pubuserdefnvc2 = data.getDynamicPropertyValues().get(pubuserdefnvc2Index);

        int pubuserdefnvc3Index = dynamicPropertyKeys.indexOf("pubuserdefnvc3");
        String pubuserdefnvc3 = data.getDynamicPropertyValues().get(pubuserdefnvc3Index);
        int pubuserdefnvc6Index = dynamicPropertyKeys.indexOf("pubuserdefnvc6");
        String pubuserdefnvc6 = data.getDynamicPropertyValues().get(pubuserdefnvc6Index);

        List<CustomField> customFields = new ArrayList<>();
        customFields.add(new CustomField("cust_field2__c", data.getDeliveryMode() == null ? null : data.getDeliveryMode().getName()));
        customFields.add(new CustomField("cust_field3__c", data.getDistributionMode().getName()));
        customFields.add(new CustomField("cust_field4__c", pubuserdefnvc1));
        customFields.add(new CustomField("cust_field5__c", pubuserdefnvc2));
        customFields.add(new CustomField("cust_field6__c", pubuserdefnvc3));
        customFields.add(new CustomField("cust_field7__c", pubuserdefnvc6));
        customFields.add(new CustomField("cust_field10__c",data.getMaker()));
        saleOrderHeihu.setCustomFields(customFields);

        return saleOrderHeihu;
    }


    @Override
    public String getType() {
        return MsgType.SALEORDER_AUDIT;
    }
}
