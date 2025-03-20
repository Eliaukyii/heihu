package com.example.business.util;

import com.example.business.constant.MsgInfoData;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.params.TokenFileParams;
import com.example.business.domain.request.AuthRequest;
import com.example.business.domain.response.ErpAuthResponse;
import com.example.business.domain.response.HeihuAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenUtil {


    public static ApiParamsErp apiParamsErp = new ApiParamsErp();
    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static TokenFileParams tokenFileParams = new TokenFileParams();


    public static ErpAuthResponse getErpToken(){

        if (MsgInfoData.MSG_INFO == null || MsgInfoData.MSG_INFO.getBizContent() == null || MsgInfoData.MSG_INFO.getBizContent().getAppTicket() == null){
            log.error("{}类型的消息推送格式有误", MsgType.APP_TICKET);
            return new ErpAuthResponse();
        }

        String appTicket = MsgInfoData.MSG_INFO.getBizContent().getAppTicket();
        log.info("请求token，appTicket = " + appTicket);

        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        AuthRequest requestBody = new AuthRequest(
                appTicket,
                apiParamsErp.certificate
        );

        ErpAuthResponse result = webClient.post()
                .uri(apiParamsErp.uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ErpAuthResponse.class)
                .block();
        log.info("请求Erp-token响应数据：" + result);

        //存储token等数据
//        SaveToken.erpAuthResponse = result;
        SaveToken.erpToken = result.getValue().getAccessToken();

        try {
            String tokenStr = SaveToken.erpToken;

            //确保目录存在
            Files.createDirectories(Paths.get(tokenFileParams.filePath));

            File file = new File(tokenFileParams.filePath, tokenFileParams.fileName);
            try (PrintStream ps = new PrintStream(new FileOutputStream(file))) {
                ps.println(tokenStr);
            }
            log.info("获取并写入token：" + tokenStr);
        } catch (IOException e) {
            log.error("token写入文件失败：" + e.getMessage());
        }

        return result;
    }

    public static HeihuAuthResponse getHeihuToken(){

        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.tokenUrl)
                .defaultHeader("appKey", apiParamsHeihu.appKey)
                .defaultHeader("appSecret", apiParamsHeihu.appSecret)
                .defaultHeader("factoryNumber", apiParamsHeihu.factoryNumber)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, Object> map = new HashMap<>();
        map.put("appKey", apiParamsHeihu.appKey);
        map.put("appSecret", apiParamsHeihu.appSecret);
        map.put("factoryNumber", apiParamsHeihu.factoryNumber);

        HeihuAuthResponse result = webClient.post()
                .bodyValue(map)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block();
        log.info("请求黑湖-token响应数据：" + result);

        return result;
    }


}
