package com.example.business.domain.msg;

import com.example.business.domain.other.Inventory;
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

    private Inventory inventory;

}
