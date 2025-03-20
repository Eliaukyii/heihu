package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class items {
    //行号
    private Integer lineNo;
    //物料编号
    private String materialCode;
    //数量
    private String amount;
    //单位
    private String unit;
    //交货日期
    private String deliveryDate;


}
