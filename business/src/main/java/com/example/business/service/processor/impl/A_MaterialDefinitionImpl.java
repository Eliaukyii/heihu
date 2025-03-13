package com.example.business.service.processor.impl;

import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.MaterialDefinitionErp;
import com.example.business.domain.MaterialDefinitionHeihu;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.service.processor.Processor;
import com.example.business.util.MaterialUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 物料定义（存货新增）
 */
@Component
@Slf4j
public class A_MaterialDefinitionImpl implements Processor {

    @Override
    public void handle(MsgInfo msgInfo) {
        log.info("程序走至：物料定义-存货新增");
        //代码实现逻辑
        MaterialUtil.MaterialHandle(msgInfo);

    }



    @Override
    public String getType() {
        return MsgType.INVENTORY_CREATE;
    }
}
