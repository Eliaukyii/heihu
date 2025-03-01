package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.domain.MsgInfo;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 采购订单
 */
@Component
@Slf4j
public class E_PurchaseOrderImpl implements Processor {
    @Override
    public void handle(MsgInfo msgInfo) {
        //代码实现逻辑
        log.info("程序走至：采购订单");
    }

    @Override
    public String getType() {
        return MsgType.PURCHASEORDER_AUDIT;
    }
}
