package com.example.business.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "params")
@Data
public class ApiParams {

    private String url;
    private String uri;
    private String appKey;
    private String appSecret;
    private String certificate;

}