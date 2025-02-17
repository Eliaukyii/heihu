package com.example.business.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "params")
@Component
@Data
public class ApiParams {
    public static String url;
    public static String uri;
    public static String appKey;
    public static String appSecret;
    public static String certificate;


    @Value("${params.url}")
    public void setUrl(String url){
        ApiParams.url = url;
    }

    @Value("${params.uri}")
    public void setUri(String uri) {
        ApiParams.uri = uri;
    }

    @Value("${params.appKey}")
    public void setAppKey(String appKey) {
        ApiParams.appKey = appKey;
    }

    @Value("${params.appSecret}")
    public void setAppSecret(String appSecret) {
        ApiParams.appSecret = appSecret;
    }

    @Value("${params.certificate}")
    public void setCertificate(String certificate) {
        ApiParams.certificate = certificate;
    }




}