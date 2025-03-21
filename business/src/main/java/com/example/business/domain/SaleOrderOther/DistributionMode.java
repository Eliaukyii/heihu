package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DistributionMode {
    //配送方式 配送方式编码Code
    //02 其他
    //00 自配送
    //01 三方物流
    @JsonProperty("Code")
    private String Code;
    @JsonProperty("Name")
    private String Name;
}
