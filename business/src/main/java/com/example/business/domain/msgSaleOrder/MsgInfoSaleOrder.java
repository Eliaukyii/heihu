package com.example.business.domain.msgSaleOrder;

import lombok.Data;

@Data
public class MsgInfoSaleOrder {
    private String code;
    private String message;
    private MsgInfoSaleOrderData data;
    private String exception;

}
