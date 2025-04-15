package com.example.business.domain.msgPurchaseOrder;

import com.example.business.domain.PurchaseOrderOther.Partner;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MsgInfoPurchaseOrderData {
    //单据Code
    @JsonProperty("Code")
    private String Code;
    //供应商
    @JsonProperty("Partner")
    private Partner Partner;
    //采购订单
    @JsonProperty("PurchaseOrderDetails")
    private List<PurchaseOrderDetails> PurchaseOrderDetails;

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("Memo")
    private String Memo;
}
