package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 物料定义（存货修改）
 */
@Component
@Slf4j
public class AA_MaterialDefinitionImpl implements Processor {


    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：物料定义-存货修改");
    }

    @Override
    public String getType() {
        return MsgType.INVENTORY_UPDATE;
    }
}
