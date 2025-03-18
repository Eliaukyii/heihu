package com.example.business.constant;

import com.example.business.domain.response.ErpAuthResponse;
import com.example.business.domain.response.HeihuAuthResponse;
import com.example.business.domain.response.HeihuAuthResponseDataBody;
import com.example.business.util.TokenUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class SaveToken {

    @Autowired
    private static ObjectMapper objectMapper;

    /**
     * 黑湖token，设置有效期为1小时
     */
    public static long heihuTimeSeconds = 1 * 60 * 60 *1000L;

    /**
     * erp的token，大约一天刷新一次
     */
    public static long erpTimeSeconds = 24 * 60 * 60 *1000L;


    /**
     * 存储erp的token，有完整的一套token存储机制
     */
    public static String erpToken;

    /**
     * 存储黑湖token
     */
    private static String heihuToken;

    /**
     * 存储黑湖token的时间
     */
    private static Date heihuTokenTime;


    public static String getHeihuToken(){
        Date now = new Date();
        if (heihuTokenTime == null || (now.getTime() - heihuTokenTime.getTime()) > heihuTimeSeconds) {
            HeihuAuthResponse response = TokenUtil.getHeihuToken();
            HeihuAuthResponseDataBody dataBody = null;
            try {
                dataBody = objectMapper.readValue(response.getData().toString(), HeihuAuthResponseDataBody.class);
            } catch (Exception e) {
                log.error("获取黑湖token时发生异常，异常信息：" + e.getMessage());
            }
            heihuToken =  dataBody.getAppAccessToken();
            heihuTokenTime = new Date();
            log.info("黑湖token过期 - 重新请求的数据：" + heihuToken);
            log.info("黑湖token过期 - 重新请求的时间：" + heihuTokenTime);
            return heihuToken;
        } else {
            log.info("黑湖token未过期，无需重新获取");
            log.info("黑湖token未过期，无需重新获取，上次获取token时间：" + heihuTokenTime);
            return heihuToken;
        }
    }

}
