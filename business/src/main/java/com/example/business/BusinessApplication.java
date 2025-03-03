package com.example.business;

import com.example.business.constant.SaveToken;
import com.example.business.domain.params.TokenFileParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
@Slf4j
public class BusinessApplication {

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
    public static TokenFileParams tokenFileParams = new TokenFileParams();

    @PostConstruct
    public void init() {
        //todo 读取token文件
        String tokenFilePath = "/var/app/files/token.txt";
        try {
            Files.createDirectories(Paths.get("/var/app/files/"));

            // 如果 token 文件不存在，创建一个新文件
            Path tokenPath = Paths.get(tokenFilePath);
            if (!Files.exists(tokenPath)) {
                Files.createFile(tokenPath);
                log.info("token.txt文件不存在，已自动创建: {}", tokenFilePath);
            }

            // 读取整个文件内容到字符串
            byte[] bytes = Files.readAllBytes(Paths.get(tokenFilePath));
            String tokenStr = new String(bytes);
            String token = objectMapper.readValue(tokenStr, String.class);
            SaveToken.erpToken = token;
        } catch (IOException e) {
            log.error("启动项目 - 读取token失败，文件路径：{}，失败原因：{}", tokenFilePath, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
