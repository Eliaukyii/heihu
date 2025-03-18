package com.example.business.domain.request;

import lombok.Data;


@Data
public class RequestParamErp {

    private RequestParamBodyErp param;

    public RequestParamErp() {

    }

    public RequestParamErp(String code, String selectFields) {
        this.param = new RequestParamBodyErp();
        this.param.setCode(code);
        this.param.setSelectFields(selectFields);
    }

    public RequestParamErp(String voucherCode) {
        this.param = new RequestParamBodyErp();
        this.param.setVoucherCode(voucherCode);
    }

}
