package com.example.business.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "params")
@Component
@Data
public class ApiParamsHeihu {
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
    public static String factoryNumber;
//    public static String certificate;


    @Value("${paramsHeihu.tokenUrl}")
    public void setTokenUrl(String tokenUrl){
        ApiParamsHeihu.tokenUrl = tokenUrl;
    }

    @Value("${paramsHeihu.url}")
    public void setUrl(String url){
        ApiParamsHeihu.url = url;
    }

    @Value("${paramsHeihu.materialUri}")
    public void setMaterialUri(String materialUri) {
        ApiParamsHeihu.materialUri = materialUri;
    }
    @Value("${paramsHeihu.bomUri}")
    public void setBomUri(String bomUri) {
        ApiParamsHeihu.bomUri = bomUri;
    }
    @Value("${paramsHeihu.customerUri}")
    public void setCustomerUri(String customerUri) {
        ApiParamsHeihu.customerUri = customerUri;
    }
    @Value("${paramsHeihu.supplierUri}")
    public void setSupplierUri(String supplierUri) {
        ApiParamsHeihu.supplierUri = supplierUri;
    }
    @Value("${paramsHeihu.saleOrderUri}")
    public void setSaleOrderUri(String saleOrderUri) {
        ApiParamsHeihu.saleOrderUri = saleOrderUri;
    }
    @Value("${paramsHeihu.purchaseOrderUri}")
    public void setPurchaseOrderUri(String purchaseOrderUri) {
        ApiParamsHeihu.purchaseOrderUri = purchaseOrderUri;
    }

    @Value("${paramsHeihu.appKey}")
    public void setAppKey(String appKey) {
        ApiParamsHeihu.appKey = appKey;
    }

    @Value("${paramsHeihu.appSecret}")
    public void setAppSecret(String appSecret) {
        ApiParamsHeihu.appSecret = appSecret;
    }

    @Value("${paramsHeihu.factoryNumber}")
    public void setFactoryNumber(String factoryNumber) {
        ApiParamsHeihu.factoryNumber = factoryNumber;
    }




}