package com.example.business.scheduler;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.business.constant.SaveToken;
import com.example.business.domain.AuthResponse;
import com.example.business.util.TokenUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

@Component
public class TokenScheduler {

    @Scheduled(cron = "0 */1 * * * ?")
    public void getToken() throws FileNotFoundException {
        AuthResponse token = TokenUtil.getToken();
        SaveToken.authResponse = token;
        JSON json = JSONUtil.parse(token);
        String tokenStr = json.toString();

        File directory = new File("/var/app/files");

        // 确保目录存在
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("文件夹已创建: " + directory.getAbsolutePath());
            } else {
                System.out.println("文件夹创建失败");
            }
        }

        File file = new File(directory, "token.txt");

        PrintStream ps = new PrintStream(new FileOutputStream(file));
        ps.println(tokenStr);

    }
}
