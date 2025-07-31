package com.example.business.api;

import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.response.ErpAuthResponse;
import com.example.business.service.processor.ProcessorService;
import com.example.business.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Test01Controller {

    @Autowired
    private ProcessorService processorService;

    /**
     * 用于测试是否能正常获取token
     * @return
     */
    @PostMapping("/getToken")
    public ErpAuthResponse getToken(){
        ErpAuthResponse token = TokenUtil.getErpToken();

        return token;
    }

    /**
     * 模拟消息订阅
     */
    @PostMapping("/haha")
    public Map getMap(@RequestBody MsgInfo msgInfo) throws Exception {

        //根据msgType的不同调用不同的实现类
        processorService.execute(msgInfo);
        Map<String, Object> map = new HashMap<>();
        map.put("result", "success");
        return map;
    }

    /**
     * 测试获取msg数据
     * @return
     */
//    @GetMapping("/getMsg")
//    public MsgInfo getMsg(){
//        return MsgType.MSG_INFO;
//    }


}
