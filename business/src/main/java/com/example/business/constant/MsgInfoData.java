package com.example.business.constant;

import com.example.business.domain.MsgInfo;
import lombok.Data;

@Data
public class MsgInfoData {

    //todo 公共的，需注意风险
    /**
     * 仅存储验证消息
     */
    public static MsgInfo MSG_INFO = new MsgInfo();


}
