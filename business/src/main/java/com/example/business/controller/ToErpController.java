package com.example.business.controller;

import com.example.business.domain.ApiParams;
import com.example.business.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * 黑湖推送至ERP
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ToErpController {

    public static ApiParams apiParams = new ApiParams();

    /**
     *
     * @param message
     * @return
     */
    @PostMapping("/otherDispatchCreate")
    public R otherDispatchCreate(@RequestBody String message){
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParams.url)
                .defaultHeader("openToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiMzk0ODIzNjg2NDMwMzYxIiwib3JnSWQiOiIxMjM5MzQ2MjE1OTg1NDgwIiwiYWNjZXNzX3Rva2VuIjoiYmMtNTY2N2JmODItYTNiMS00ZDc5LWJiZWQtZmZiMTc1MzAyNTUwIiwiYXVkIjoiaXN2IiwibmJmIjoxNzM5OTc3ODk0LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImFwcEtleSI6IjFxeHlwT21yIiwiaWQiOiI4YWRhNDQyYy0yZDcwLTQ2Y2YtYThkZi1kYjNlNWYxMmZmNWQiLCJleHAiOjE3NDA0OTYyOTQsImlhdCI6MTczOTk3Nzg5NCwib3JnQWNjb3VudCI6InVyaWhua20zMml1biJ9.AoEBKOxSR8h3wyw-mwW-Mmj-yJtR9CgruCL2_CsRWIg")
                .defaultHeader("appKey", apiParams.appKey)
                .defaultHeader("appSecret", apiParams.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String result = webClient.post()
                //                .uri(apiParams.uri)
                .uri("/tplus/api/v2/otherDispatch/Create")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .toString();
        log.info("请求token响应数据：" + result);

        return R.ok();
    }


}
