package com.example.business.util;

import com.example.business.constant.MsgType;
import com.example.business.domain.AuthRequest;
import com.example.business.domain.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class TokenUtil {

    @Value("${params.url}")
    private static String url;
    @Value("${params.uri}")
    private static String uri;
    @Value("${params.appKey}")
    private static String appKey;
    @Value("${params.appSecret}")
    private static String appSecret;
    @Value("${params.certificate}")
    private static String certificate;



    public static AuthResponse getToken(){
        String appTicket = MsgType.MSG_INFO.getBizContent().getAppTicket();
        log.info("请求token，appTicket = " + appTicket);

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("appKey", appKey)
                .defaultHeader("appSecret", appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        AuthRequest requestBody = new AuthRequest(
                appTicket,
                certificate
        );

        AuthResponse result = webClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(AuthResponse.class)
                .block(); //todo .block()谨慎使用，后续再分析是否使用
        log.info("请求token响应数据：" + result);

        return result;
    }


}
