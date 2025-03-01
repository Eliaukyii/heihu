package com.example.business.service.processor;

import com.example.business.domain.MsgInfo;
import com.example.business.service.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessorService {

    private final Map<String, Processor> processorMap;

    // 通过构造器注入所有Processor实现
    @Autowired
    public ProcessorService(List<Processor> processors) {
        processorMap = processors.stream()
                .collect(Collectors.toMap(Processor::getType, Function.identity()));
    }

    public void execute(MsgInfo msgInfo) {
        String msgType = msgInfo.getMsgType();
        Processor processor = processorMap.get(msgType);
        if (processor == null) {
//            throw new IllegalArgumentException("未知类型: " + msgType);
            log.error("未知类型: " + msgType);
            return;
        }
        processor.handle(msgInfo);
    }

}
