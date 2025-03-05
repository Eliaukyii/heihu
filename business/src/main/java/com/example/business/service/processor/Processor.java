package com.example.business.service.processor;

import com.example.business.domain.msg.MsgInfo;

public interface Processor {

    void handle(MsgInfo msgInfo);

    String getType();

}
