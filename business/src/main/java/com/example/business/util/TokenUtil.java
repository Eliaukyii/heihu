package com.example.business.util;

import com.example.business.constant.MsgInfoData;
import com.example.business.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenUtil {


    public static ApiParamsErp apiParamsErp = new ApiParamsErp();
    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();


    public static ErpAuthResponse getErpToken(){
        String appTicket = MsgInfoData.MSG_INFO.getBizContent().getAppTicket();
        log.info("请求token，appTicket = " + appTicket);

        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        AuthRequest requestBody = new AuthRequest(
                appTicket,
                apiParamsErp.certificate
        );

        ErpAuthResponse result = webClient.post()
                .uri(apiParamsErp.uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ErpAuthResponse.class)
                .block(); //todo .block()谨慎使用，后续再分析是否使用
        log.info("请求Erp-token响应数据：" + result);

        return result;
    }

    public static HeihuAuthResponse getHeihuToken(){

        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.tokenUrl)
                .defaultHeader("appKey", apiParamsHeihu.appKey)
                .defaultHeader("appSecret", apiParamsHeihu.appSecret)
                .defaultHeader("factoryNumber", apiParamsHeihu.factoryNumber)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, Object> map = new HashMap<>();
        map.put("appKey", apiParamsHeihu.appKey);
        map.put("appSecret", apiParamsHeihu.appSecret);
        map.put("factoryNumber", apiParamsHeihu.factoryNumber);

        HeihuAuthResponse result = webClient.post()
                .bodyValue(map)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block(); //todo .block()谨慎使用，后续再分析是否使用
        log.info("请求黑湖-token响应数据：" + result);

        return result;
    }


}
