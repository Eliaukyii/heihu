package com.example.business.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
@Data
public class OtherDispatchCreateParams {
    private static String Url;
    private static String Uri;
    private static String appKey;
    private static String appSecret;

//    @Value("${params.url}")
    public void setUrl(String url){
        ApiParamsHeihu.url = url;
    }

//    @Value("${OtherDispatchCreateParams.uri}")
    public void setOtherDispatchCreateUri(String uri) {
        OtherDispatchCreateParams.Uri = Uri;
    }

//    @Value("${params.appKey}")
    public void setAppKey(String appKey) {
        ApiParamsHeihu.appKey = appKey;
    }

//    @Value("${params.appSecret}")
    public void setAppSecret(String appSecret) {
        ApiParamsHeihu.appSecret = appSecret;
    }
}

