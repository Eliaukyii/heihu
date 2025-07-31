package com.example.business.domain.msgPurchaseOrder;

import com.example.business.domain.PurchaseOrderOther.Partner;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MsgInfoPurchaseOrderData {
    //单据Code
    private String Code;
    //供应商
    private Partner Partner;
    //采购订单
    private List<PurchaseOrderDetails> PurchaseOrderDetails;

    private Integer ID;

    private String Memo;
}
