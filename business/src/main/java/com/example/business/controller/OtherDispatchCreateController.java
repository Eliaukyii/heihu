package com.example.business.controller;

import cn.hutool.json.JSONObject;
import com.example.business.domain.ApiParams;
import com.example.business.domain.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/OtherDispatchCreateApi")
@Slf4j
public class OtherDispatchCreateController {

    public static ApiParams apiParams = new ApiParams();

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/OtherDispatchCreate")
    public ResponseEntity<String>  OtherDispatchCreate(@RequestBody Map<String,Object> message){
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            System.out.println(jsonMessage);

            //请求erp
//            this.requestMethod(message);
            getRe(jsonMessage);

            return new ResponseEntity<>(jsonMessage, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error converting message to JSON",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void getRe(String s){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("openToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiMzk0ODIzNjg2NDMwMzYxIiwib3JnSWQiOiIxMjM5MzQ2MjE1OTg1NDgwIiwiYWNjZXNzX3Rva2VuIjoiYmMtNTY2N2JmODItYTNiMS00ZDc5LWJiZWQtZmZiMTc1MzAyNTUwIiwiYXVkIjoiaXN2IiwibmJmIjoxNzM5OTc3ODk0LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImFwcEtleSI6IjFxeHlwT21yIiwiaWQiOiI4YWRhNDQyYy0yZDcwLTQ2Y2YtYThkZi1kYjNlNWYxMmZmNWQiLCJleHAiOjE3NDA0OTYyOTQsImlhdCI6MTczOTk3Nzg5NCwib3JnQWNjb3VudCI6InVyaWhua20zMml1biJ9.AoEBKOxSR8h3wyw-mwW-Mmj-yJtR9CgruCL2_CsRWIg");
        headers.set("appKey", "1qxypOmr");
        headers.set("appSecret", "01E398317E2C2C634D90FA931EFFA92D");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(s, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://openapi.chanjet.com/tplus/api/v2/otherDispatch/Create",
                request,
                String.class
        );
        String body = response.getBody();
        log.info("响应数据："+body);

    }



    private void requestMethod(Map<String,Object> message) {

        WebClient webClient = WebClient.builder()
                .baseUrl(apiParams.url)
                .defaultHeader("openToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiMzk0ODIzNjg2NDMwMzYxIiwib3JnSWQiOiIxMjM5MzQ2MjE1OTg1NDgwIiwiYWNjZXNzX3Rva2VuIjoiYmMtNTY2N2JmODItYTNiMS00ZDc5LWJiZWQtZmZiMTc1MzAyNTUwIiwiYXVkIjoiaXN2IiwibmJmIjoxNzM5OTc3ODk0LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImFwcEtleSI6IjFxeHlwT21yIiwiaWQiOiI4YWRhNDQyYy0yZDcwLTQ2Y2YtYThkZi1kYjNlNWYxMmZmNWQiLCJleHAiOjE3NDA0OTYyOTQsImlhdCI6MTczOTk3Nzg5NCwib3JnQWNjb3VudCI6InVyaWhua20zMml1biJ9.AoEBKOxSR8h3wyw-mwW-Mmj-yJtR9CgruCL2_CsRWIg")
                .defaultHeader("appKey", apiParams.appKey)
                .defaultHeader("appSecret", apiParams.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            String result = webClient.post()
    //                .uri(apiParams.uri)
                    .uri("/tplus/api/v2/otherDispatch/Create")
                    .bodyValue(message)
                    .retrieve()

                    .onStatus(status -> !status.is2xxSuccessful(),
                            response -> response.bodyToMono(String.class).map(Exception::new))
                    .bodyToMono(String.class)
                    .block(); //todo .block()谨慎使用，后续再分析是否使用
            log.info("请求token响应数据：" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
