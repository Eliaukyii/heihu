package com.example.business.domain.response;

import lombok.Data;

@Data
public class ErpAuthResponseValueBody {

    private String accessToken;

    private String refreshToken;

    private String scope;

    private Long expiresIn;

    private String userId;

    private String orgId;

    private String appName;

    private Long refreshExpiresIn;

}
