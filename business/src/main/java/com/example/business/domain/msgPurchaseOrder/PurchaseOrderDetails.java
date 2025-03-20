package com.example.business.domain.msgPurchaseOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PurchaseOrderDetails {
    //物料编号
    @JsonProperty("PartnerInventoryCode")
    private String PartnerInventoryCode;
    //数量
    @JsonProperty("Quantity")
    private String Quantity;
    //计量单位
    @JsonProperty("Unit")
    private Unit Unit;
}
