package com.example.business.constant;

import com.example.business.domain.response.ErpAuthResponse;
import com.example.business.domain.response.HeihuAuthResponse;

public class SaveToken {

    //todo 启动项目后，执行一次方法，将上次的token赋值
    public static ErpAuthResponse erpAuthResponse;

    public static String erpToken;

    public static HeihuAuthResponse heihuAuthResponse;

    public static String heihuToken;

}
