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
    public static String tokenUrl;
    public static String materialUri;
    public static String bomUri;
    public static String customerUri;
    public static String supplierUri;
    public static String saleOrderUri;
    public static String purchaseOrderUri;
    public static String appKey;
    public static String appSecret;
//    public static String certificate;


    @Value("${params.tokenUrl}")
    public void setTokenUrl(String tokenUrl){
        ApiParams.tokenUrl = tokenUrl;
    }

    @Value("${params.url}")
    public void setUrl(String url){
        ApiParams.url = url;
    }

    @Value("${params.materialUri}")
    public void setMaterialUri(String materialUri) {
        ApiParams.materialUri = materialUri;
    }
    @Value("${params.bomUri}")
    public void setBomUri(String bomUri) {
        ApiParams.bomUri = bomUri;
    }
    @Value("${params.customerUri}")
    public void setCustomerUri(String customerUri) {
        ApiParams.customerUri = customerUri;
    }
    @Value("${params.supplierUri}")
    public void setSupplierUri(String supplierUri) {
        ApiParams.supplierUri = supplierUri;
    }
    @Value("${params.saleOrderUri}")
    public void setSaleOrderUri(String saleOrderUri) {
        ApiParams.saleOrderUri = saleOrderUri;
    }
    @Value("${params.purchaseOrderUri}")
    public void setPurchaseOrderUri(String purchaseOrderUri) {
        ApiParams.purchaseOrderUri = purchaseOrderUri;
    }

    @Value("${params.appKey}")
    public void setAppKey(String appKey) {
        ApiParams.appKey = appKey;
    }

    @Value("${params.appSecret}")
    public void setAppSecret(String appSecret) {
        ApiParams.appSecret = appSecret;
    }




}