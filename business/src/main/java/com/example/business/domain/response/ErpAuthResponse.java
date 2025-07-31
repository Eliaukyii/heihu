package com.example.business.domain.response;

import lombok.Data;

@Data
public class ErpAuthResponse {

    private boolean result;

    private Object error;

    private ErpAuthResponseValueBody value;

    private String traceId;

}

