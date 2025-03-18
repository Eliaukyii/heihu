package com.example.business.domain.SaleOrderOther;

import lombok.Data;

@Data
public class DistributionMode {
    //配送方式 配送方式编码Code
    //02 其他
    //00 自配送
    //01 三方物流
    private String Code;
    private String Name;
}
