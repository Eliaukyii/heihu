package com.example.business.constant;

import com.example.business.domain.MsgInfo;

public class MsgType {

//    public static String APP_TICKET = "APP_TICKET";

//    public static String MSG_TYPE = MsgType.MSG_INFO.getMsgType();

    public static MsgInfo MSG_INFO = new MsgInfo();

    /**
     * 存货新增
     */
    public static String INVENTORY_CREATE = "Inventory_Create";

    /**
     * 物料清单新增
     */
    public static String BOM_CREATE = "Bom_Create";

    /**
     * 往来单位新增
     */
    public static String PARTNER_CREATE = "Partner_Create";

    /**
     * 销售订单审核
     */
    public static String SALEORDER_AUDIT = "SaleOrder_Audit";

    /**
     * 采购订单审核
     */
    public static String PURCHASEORDER_AUDIT = "PurchaseOrder_Audit";


}
