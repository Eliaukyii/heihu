package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 销售订单
 */
@Component
@Slf4j
public class D_SaleorderImpl implements Processor {
    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：销售订单");
    }

    @Override
    public String getType() {
        return MsgType.SALEORDER_AUDIT;
    }
}
