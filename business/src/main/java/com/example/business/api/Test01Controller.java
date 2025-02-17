package com.example.business.api;

import com.example.business.constant.MsgType;
import com.example.business.domain.AuthResponse;
import com.example.business.domain.MsgInfo;
import com.example.business.util.TokenUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Test01Controller {

    /**
     * 用于测试是否能正常获取token
     * @return
     */
    @PostMapping("/getToken")
    public AuthResponse getToken(){
        AuthResponse token = TokenUtil.getToken();

        return token;
    }

    /**
     * 测试获取msg数据
     * @return
     */
    @GetMapping("/getMsg")
    public MsgInfo getMsg(){
        return MsgType.MSG_INFO;
    }


}
