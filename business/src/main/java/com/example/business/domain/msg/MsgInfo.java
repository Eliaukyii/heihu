package com.example.business.domain.msg;

import lombok.Data;

@Data
public class MsgInfo {

    private String id;

    private String appKey;

    private String appId;

    private String msgType;

    private String time;

    private MsgInfoBizContent bizContent;

    private String orgId;

    private String requestId;


}
