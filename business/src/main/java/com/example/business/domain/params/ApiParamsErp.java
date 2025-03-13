package com.example.business.domain.params;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApiParamsErp {
    public static String url;
    public static String uri;
    public static String appKey;
    public static String appSecret;
    public static String certificate;
    public static String customerUri;
    public static String bomUri;
    public static String routingUri;


    @Value("${paramsErp.url}")
    public void setUrl(String url) {
        ApiParamsErp.url = url;
    }

    @Value("${paramsErp.uri}")
    public void setUri(String uri) {
        ApiParamsErp.uri = uri;
    }

    @Value("${paramsErp.appKey}")
    public void setAppKey(String appKey) {
        ApiParamsErp.appKey = appKey;
    }

    @Value("${paramsErp.appSecret}")
    public void setAppSecret(String appSecret) {
        ApiParamsErp.appSecret = appSecret;
    }

    @Value("${paramsErp.certificate}")
    public void setCertificate(String certificate) {
        ApiParamsErp.certificate = certificate;
    }

    @Value("${paramsErp.customerUri}")
    public void setCustomerUri(String customerUri) {
        ApiParamsErp.customerUri = customerUri;
    }

    @Value("${paramsErp.bomUri}")
    public void setBomUri(String bomUri) {
        ApiParamsErp.bomUri = bomUri;
    }

    @Value("${paramsErp.routingUri}")
    public void setRoutingUri(String routingUri) {
        ApiParamsErp.routingUri = routingUri;
    }


}
