package com.example.business.domain.request;

import lombok.Data;

@Data
public class RequestParamErpPurchase {
    private RequestParamBodyErpPurchase param;

    public RequestParamErpPurchase(String voucherCode){
        this.param = new RequestParamBodyErpPurchase();
        this.param.setVoucherCode(voucherCode);
    }

}
