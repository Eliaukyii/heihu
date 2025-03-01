package com.example.business.domain;

import lombok.Data;

import java.util.HashMap;

@Data
public class MsgInfoBizContent {

    private String code;

    private Object marketingOrgan;

    private String name;

    private String id;

    private MsgInfoBizContentPartnerType partnerType;

    private String ts;

    private String appTicket;

}
