package com.example.business.service.processor.impl;

import com.example.business.service.processor.Processor;
import org.springframework.stereotype.Component;

@Component
public class C_CustomerImpl implements Processor {
    @Override
    public void handle() {
        //代码实现逻辑
    }

    @Override
    public String getType() {
        return null;
    }
}
