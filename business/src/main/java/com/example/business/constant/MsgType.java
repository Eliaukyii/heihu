package com.example.business.constant;

import com.example.business.domain.MsgInfo;

public class MsgType {

//    public static String APP_TICKET = "APP_TICKET";

    public static String MSG_TYPE = MsgType.MSG_INFO.getMsgType();

    public static MsgInfo MSG_INFO = new MsgInfo();

    public static String INVENTORY_CREATE = "Inventory_Create";
    public static String BOM_CREATE = "Bom_Create";
    public static String BOM_UPDATE = "Bom_Update";


}
