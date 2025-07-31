package com.example.business.domain;

import com.example.business.domain.params.ApiParamsHeihu;
import lombok.Data;

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

