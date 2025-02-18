package com.example.business.service.processor;

import com.example.business.service.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessorService {

    private final Map<String, Processor> processorMap;

    // 通过构造器注入所有Processor实现
    @Autowired
    public ProcessorService(List<Processor> processors) {
        processorMap = processors.stream()
                .collect(Collectors.toMap(Processor::getType, Function.identity()));
    }

    public void execute(String type) {
        Processor processor = processorMap.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("未知类型: " + type);
        }
        processor.handle();
    }

}
