package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.service.processor.Processor;
import org.springframework.stereotype.Component;

/**
 * 物料清单（修改）
 */
@Component
public class BB_MaterialListImpl implements Processor {
    @Override
    public void handle() {
        //代码实现逻辑
    }

    @Override
    public String getType() {
        return MsgType.BOM_UPDATE;
    }
}
