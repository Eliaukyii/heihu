package com.example.business.domain.msgPurchaseOrder;

import lombok.Data;

@Data
public class PurchaseOrderDetails {
    //物料编号
    private String PartnerInventoryCode;
    //数量
    private String Quantity;
    //计量单位
    private Unit Unit;
}
