package com.example.business.domain;
import com.example.business.domain.SaleOrderOther.items;
import com.example.business.domain.SaleOrderOther.CustomField;
import lombok.Data;

import java.util.List;

@Data
public class SaleOrderHeihu {
    //订单编号
    private String code;
    //客户名称
    private String customerCode;
    //出货方式
    private String outboundType;
    //订单所有人
    private String ownerCode;
    //地址信息
    private String receiveInformation;
    //联系人
    private String contactName;
    //手机号码
    private String phoneNumber;

    private List<items> items;

    private List<CustomField> customFields;
    private String remark;


}
