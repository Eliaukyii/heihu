package com.example.business.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpUtil {


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


}
