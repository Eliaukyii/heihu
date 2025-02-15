package com.example.business.domain;

import lombok.Data;

@Data
public class AuthResponse {

    private boolean result;

    private Object error;

    private ValueBody value;

    private String traceId;

}

@Data
class ValueBody{

    private String accessToken;

    private String refreshToken;

    private String scope;

    private Long expiresIn;

    private String userId;

    private String orgId;

    private String appName;

    private Long refreshExpiresIn;

}
