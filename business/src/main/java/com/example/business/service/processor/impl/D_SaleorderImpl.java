package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.SaleOrderHeihu;
import com.example.business.domain.SaleOrderOther.SaleOrderDetails;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrder;
import com.example.business.domain.msgSaleOrder.MsgInfoSaleOrderData;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.SaleOrderOther.items;
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
                .defaultHeader("appKey",apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        RequestParamErp requestParamErp = new RequestParamErp(voucherCode);

        MsgInfoSaleOrder msgInfoSaleOrder1 = webClientErp.post()
                .uri(apiParamsErp.saleOrderUri)
                .bodyValue(requestParamErp)
                .retrieve()
                .bodyToMono(MsgInfoSaleOrder.class)
                .block();
        log.info("销售订单,销售订单新增 - 根据voucherCode请求erp的响应数据: " + msgInfoSaleOrder1);

        //组装数据
        MsgInfoSaleOrderData data1 = msgInfoSaleOrder1.getData();
        SaleOrderHeihu data = covertRequest(data1);

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

    private  SaleOrderHeihu covertRequest(MsgInfoSaleOrderData data){
        SaleOrderHeihu saleOrderHeihu = new SaleOrderHeihu();
        saleOrderHeihu.setCustomerCode(data.getCustomer().getName());
        saleOrderHeihu.setOutboundType("直接出库");
        saleOrderHeihu.setOwnerCode(data.getAuditor());
        saleOrderHeihu.setReceiveInformation(data.getAddress());
        saleOrderHeihu.setContactName(data.getLinkMan());
        saleOrderHeihu.setPhoneNumber(data.getCustomerPhone());
        List<SaleOrderDetails> details = data.getSaleOrderDetailsList();
        List<items> itemsList = details.stream().map(detail ->{
            items items1 = new items();
            items1.setLineNo(1);
            items1.setMaterialCode(detail.getInventory().getCode());
            items1.setAmount(data.getQuantity());
            items1.setUnit(data.getUnit().getName());
            items1.setDeliveryDate(data.getDeliveryDate());
            return items1;
        }).collect(Collectors.toList());
        saleOrderHeihu.setItemsList(itemsList);
        return saleOrderHeihu;
    }





    @Override
    public String getType() {
        return MsgType.SALEORDER_AUDIT;
    }
}
