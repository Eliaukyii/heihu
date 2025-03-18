package com.example.business.domain.msgPurchaseOrder;

import com.example.business.domain.PurchaseOrderOther.Partner;
import lombok.Data;

import java.util.List;

@Data
public class MsgInfoPurchaseOrderData {
    //单据Code
    private String voucherCode;
    //供应商
    private Partner Partner;
    //采购订单
    private List<PurchaseOrderDetails> PurchaseOrderDetailsList;
    //需求时间
    private String AcceptDate;
}
