package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.msg.MsgInfoBizContent;
import com.example.business.domain.msg.MsgInfoBizContentPartnerType;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamBodyErp;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.service.processor.Processor;
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
 * 客户 and 供应商
 */
@Component
@Slf4j
public class C_CustomerImpl implements Processor {

    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    @Override
    public void handle(MsgInfo msgInfo) {
        log.info("程序走至：客户-供应商");
        //代码实现逻辑

        MsgInfoBizContent bizContent = msgInfo.getBizContent();
        String code = bizContent.getCode();
        String name = bizContent.getName();
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", name);

        MsgInfoBizContentPartnerType partnerType = bizContent.getPartnerType();
        String partnerTypeCode = partnerType.getCode();
        //请求erp获取详细信息
        WebClient webClientErp = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("openToken", SaveToken.erpToken)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

//        HashMap<String, Object> erpMap = new HashMap<>();
//        HashMap<String, Object> map1 = new HashMap<>();
//        map1.put("Code", code);
//        map1.put("SelectFields", "id,code,name,PartnerClass.Code,PartnerType.Code");
//        erpMap.put("param", map1);

        String SelectFields = "id,code,name,PartnerClass.Code,PartnerType.Code";
        RequestParamErp requestParamErp = new RequestParamErp(code, SelectFields);

        ArrayList<Object> list = new ArrayList<>();
        List<Object> ErpResponseData = webClientErp.post()
                .uri(apiParamsErp.customerUri)
                .bodyValue(requestParamErp)
                .retrieve()
                .bodyToMono(list.getClass())
                .block();
        log.info("供应商 - 根据code请求erp的响应数据：" + ErpResponseData);

        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //00供应商，01客户，02客户/供应商
        if ("00".equals(partnerTypeCode)) {

            //todo .retrieve().bodyToMono(String.class)能否去掉？
            String heihuResponse = webClient.post()
                    .uri(apiParamsHeihu.supplierUri)
                    .bodyValue(map)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("供应商 - 请求黑湖响应数据：" + heihuResponse);

        } else if ("01".equals(partnerTypeCode)){

            map.put("ownerCode", "admin");
            //todo .retrieve().bodyToMono(String.class)能否去掉？
            String heihuResponse = webClient.post()
                    .uri(apiParamsHeihu.customerUri)
                    .bodyValue(map)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("客户 - 请求黑湖响应数据：" + heihuResponse);

        } else if ("02".equals(partnerTypeCode)) {
            //暂无
        } else {
            log.error("未知的code：" + partnerTypeCode);
        }

    }

    @Override
    public String getType() {
        return MsgType.PARTNER_CREATE;
    }
}
