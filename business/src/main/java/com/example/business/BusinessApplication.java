package com.example.business;

import cn.hutool.json.JSONUtil;
import com.example.business.constant.SaveToken;
import com.example.business.domain.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BusinessApplication {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        //todo 读取token文件

        File file = new File("/var/app/files/token.txt");

        try {
            // 读取整个文件内容到字符串
            byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
            String tokenStr = new String(bytes);
            AuthResponse authResponse = objectMapper.readValue(tokenStr, AuthResponse.class);
            SaveToken.authResponse = authResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
