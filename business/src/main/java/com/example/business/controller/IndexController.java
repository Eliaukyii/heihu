package com.example.business.controller;

import com.example.business.domain.msg.Msg;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.service.processor.ProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class IndexController {

    @Autowired
    private ProcessorService processorService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 通过ip地址直接访问
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping
    public Map getMap(@RequestBody Msg request) {

        String encryptMsg = request.getEncryptMsg();
        String key = "bw2n5qqm5rclvlmp";

        byte[] bytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            bytes = Base64.decodeBase64(encryptMsg);
            bytes = cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("解密失败，错误信息：{}", e.getMessage());
            Map<String, Object> map = new HashMap<>();
            map.put("result", "fail");
            return map;
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        log.info("解密后数据：" + s);

        MsgInfo msgInfo = null;
        try {
            msgInfo = objectMapper.readValue(s, MsgInfo.class);
        } catch (JsonProcessingException e) {
            log.error("对象反序列化失败，失败信息：{}", e.getMessage());
            Map<String, Object> map = new HashMap<>();
            map.put("result", "fail");
            return map;
        }

        //根据msgType的不同调用不同的实现类
        processorService.execute(msgInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("result", "success");

        return map;
    }


}
