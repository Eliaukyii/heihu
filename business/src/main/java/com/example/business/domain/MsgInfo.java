package com.example.business.domain;

import lombok.Data;

@Data
public class MsgInfo {

    private String id;

    private String appKey;

    private String msgType;

    private String time;

    private BizContent bizContent;

}
