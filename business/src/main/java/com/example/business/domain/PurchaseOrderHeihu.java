package com.example.business.domain;

import com.example.business.domain.PurchaseOrderOther.itemList;
import com.example.business.domain.PurchaseOrderOther.upperNoteType;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderHeihu {
    //订单编号
    private String code;
    //供应商
    private String supplierCode;
    //入库方式 入库方式。1:收货后入库，2:直接入库
    private Integer defaultInStorageType;
    //订单所有人
    private String ownerCode;
    //来源  采购订单来源。0:普通采购，1:协同采购
    private Integer source;
    //上游单据类型
    private upperNoteType upperNoteType;
    //带料方式 采购订单带料方式。0:不带料，1:带料。默认为 不带料
    private Integer materialCarryMode;
    //交货方式 采购订单交货方式。0:按订单，1:按交货计划。默认为 按订单
    private Integer deliveryMode;
    //单位
    private String unitName;
    private List<itemList> itemList;
}
