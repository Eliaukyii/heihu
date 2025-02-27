package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 客户 and 供应商
 */
@Component
@Slf4j
public class C_CustomerImpl implements Processor {
    @Override
    public void handle() {
        //代码实现逻辑
        log.info("程序走至：客户-供应商");
    }

    @Override
    public String getType() {
        return MsgType.PARTNER_CREATE;
    }
}
