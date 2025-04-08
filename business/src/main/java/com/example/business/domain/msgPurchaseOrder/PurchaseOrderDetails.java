package com.example.business.domain.msgPurchaseOrder;

import com.example.business.domain.PurchaseOrderOther.Inventory;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PurchaseOrderDetails {
    //物料编号
    @JsonProperty("Inventory")
    private Inventory Inventory;
    //数量
    @JsonProperty("Quantity")
    private String Quantity;
    //计量单位
    @JsonProperty("Unit")
    private Unit Unit;
    @JsonProperty("AcceptDate")
    private String AcceptDate;

    @JsonProperty("ID")
    private Integer ID;


}
