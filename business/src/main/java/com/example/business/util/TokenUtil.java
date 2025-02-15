package com.example.business.util;

import com.example.business.domain.AuthRequest;
import com.example.business.domain.AuthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class TokenUtil {

    public static AuthResponse getToken(){
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openapi.chanjet.com")
                .defaultHeader("appKey", "1qxypOmr")
                .defaultHeader("appSecret", "01E398317E2C2C634D90FA931EFFA92D")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        AuthRequest requestBody = new AuthRequest(
                "t-24e78354d1704b8d98a97bb2d9c3d82f",
                "OXYwHSWAc22UPHxfIUM0SV1t69Ur50h2hQ5rVnS47dYxfWu0mH2IbIxVNe/lcMG7/p6QHGOiUnFjDKwHLWdR8auh/DxDywljHbo9v0hwkP+bRbEM3DvXAezmhMmaRnD/mNus3y0pDHhpJiLJvZzlkqcBssBkw2+/3wH0wM5BoZYDSpcTyLzdRlBJ9O0hOuDf"
        );

        AuthResponse result = webClient.post()
                .uri("/v1/common/auth/selfBuiltApp/generateToken")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(AuthResponse.class)
                .block();
        System.out.println(result);

        return result;
    }


}
