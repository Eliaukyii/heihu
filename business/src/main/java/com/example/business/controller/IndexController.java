package com.example.business.controller;

import com.example.business.constant.MsgType;
import com.example.business.domain.Msg;
import com.sun.prism.impl.ps.BaseShaderContext;
import org.apache.tomcat.util.codec.binary.Base64;
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
public class IndexController {

    /**
     * 通过ip地址直接访问
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping
    public Map getMap(@RequestBody Msg request) throws Exception {

        String encryptMsg = request.getEncryptMsg();
        String key = "bw2n5qqm5rclvlmp";

        Cipher cipher = Cipher.getInstance("AES/ECB/NOPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] bytes = Base64.decodeBase64(encryptMsg);
        bytes = cipher.doFinal(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);

        MsgType.APP_TICKET = s;

        System.out.println("解密后数据：" + s);

        Map<String, Object> map = new HashMap<>();
        map.put("result", "success");

        return map;
    }
}
