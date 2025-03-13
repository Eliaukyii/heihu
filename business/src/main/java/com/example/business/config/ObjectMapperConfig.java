package com.example.business.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public ObjectMapper objectMapperUpper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)  //按大写规则映射字段
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }



}
