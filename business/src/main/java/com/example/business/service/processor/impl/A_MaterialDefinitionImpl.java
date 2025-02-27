package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 物料定义
 */
@Component
@Slf4j
public class A_MaterialDefinitionImpl implements Processor {


    @Override
    public void handle() {
        //代码实现逻辑
        log.info("程序走至：物料定义-存货新增");
    }

    @Override
    public String getType() {
        return MsgType.INVENTORY_CREATE;
    }
}
