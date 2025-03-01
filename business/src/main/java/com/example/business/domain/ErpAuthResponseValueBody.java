package com.example.business.domain;

import lombok.Data;

@Data
class ErpAuthResponseValueBody {

    private String accessToken;

    private String refreshToken;

    private String scope;

    private Long expiresIn;

    private String userId;

    private String orgId;

    private String appName;

    private Long refreshExpiresIn;

}
