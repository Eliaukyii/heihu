package com.example.business.domain.response;

import lombok.Data;

@Data
public class HeihuAuthResponse {

    private Object data;

    private String code;

    private String subCode;

    private String message;

    private String needCheck;

    private String fieldPermission;


}
