package com.example.business.domain.msgPurchaseOrder;

import lombok.Data;

@Data
public class MsgInfoPurchaseOrder {
    private String code;
    private String message;
    private MsgInfoPurchaseOrderData data;
    private String exception;

}
