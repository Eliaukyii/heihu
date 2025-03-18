package com.example.business.domain.PurchaseOrderOther;

import lombok.Data;

@Data
public class itemList {
    //行号
    private String seqNum;

    //物料编号
    private String materialCode;
    //需求数量
    private String demandAmount;

    //需求时间
    private String demandTime;

}
