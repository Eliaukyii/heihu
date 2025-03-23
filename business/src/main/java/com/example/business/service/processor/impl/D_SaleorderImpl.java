package com.example.business.service.processor.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.SaleOrderHeihu;
import com.example.business.domain.SaleOrderOther.CustomField;
import com.example.business.domain.SaleOrderOther.SaleOrderDetails;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrder;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrderData;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.SaleOrderOther.items;
import com.example.business.domain.request.RequestParamErpPurchase;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
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
        String heiHuResponse = webClient.post()
                .uri(apiParamsHeihu.saleOrderUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("销售订单,销售订单新增 - 请求黑湖响应数据: " + heiHuResponse);
    }

    private SaleOrderHeihu covertRequest(MsgInfoSaleOrderData data) {
        SaleOrderHeihu saleOrderHeihu = new SaleOrderHeihu();
        saleOrderHeihu.setCode(data.getCode());
        saleOrderHeihu.setCustomerCode(data.getCustomer().getCode());
        saleOrderHeihu.setOutboundType("直接出库");
        saleOrderHeihu.setOwnerCode(data.getAuditor());
        saleOrderHeihu.setReceiveInformation(data.getAddress());
        saleOrderHeihu.setContactName(data.getLinkMan());
        saleOrderHeihu.setPhoneNumber(data.getCustomerPhone());
        List<SaleOrderDetails> details = data.getSaleOrderDetails();
        List<items> itemsList = details.stream().map(detail -> {
            items items1 = new items();
            items1.setLineNo(1);
            items1.setMaterialCode(detail.getInventory().getCode());
            items1.setAmount(detail.getQuantity());
            items1.setUnit(detail.getUnit().getName());
            items1.setDeliveryDate(detail.getDeliveryDate());
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
        customFields.add(new CustomField("cust_field2__c", data.getDeliveryMode() == null ? null : data.getDeliveryMode().getCode()));
        customFields.add(new CustomField("cust_field3__c", data.getDistributionMode().getCode()));
        customFields.add(new CustomField("cust_field4__c", pubuserdefnvc1));
        customFields.add(new CustomField("cust_field5__c", pubuserdefnvc2));
        customFields.add(new CustomField("cust_field6__c", pubuserdefnvc3));
        customFields.add(new CustomField("cust_field7__c", pubuserdefnvc6));
        saleOrderHeihu.setCustomFields(customFields);

        return saleOrderHeihu;
    }


    @Override
    public String getType() {
        return MsgType.SALEORDER_AUDIT;
    }
}
