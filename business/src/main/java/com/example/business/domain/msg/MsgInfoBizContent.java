package com.example.business.domain.msg;

import lombok.Data;

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
