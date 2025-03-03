package com.example.business.scheduler;

import com.example.business.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class TokenScheduler {

    @Scheduled(cron = "0 */1 * * * ?")
    public void getToken() {

        TokenUtil.getErpToken();

    }
}
