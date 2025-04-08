package com.example.business.domain.PurchaseOrderOther;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class itemList {
    //行号
    private String lineNo;

    //物料编号
    private String materialCode;
    //需求数量
    private String demandAmount;

    //需求时间
    private String demandTime;

    private List<CustomFields2> customFields = new ArrayList<>();

}
