package com.example.business.domain.msgSaleOrder;
import com.example.business.domain.SaleOrderOther.Customer;
import com.example.business.domain.SaleOrderOther.DistributionMode;
import com.example.business.domain.SaleOrderOther.SaleOrderDetails;
import com.example.business.domain.SaleOrderOther.Unit;
import com.example.business.domain.SaleOrderOther.DeliveryMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MsgInfoSaleOrderData {
    @JsonProperty("Code")
    private String Code;
    //客户
    @JsonProperty("Customer")
    private Customer Customer;
    //审核人
    @JsonProperty("Maker")
    private String Maker;
    @JsonProperty("Address")
    //送货地址
    private String  Address;
    @JsonProperty("LinkMan")
    //客户联系人
    private String  linkMan;
    @JsonProperty("CustomerPhone")
    //客户手机号
    private String  CustomerPhone;
    @JsonProperty("DeliveryMode")
    //运输方式
    private DeliveryMode  DeliveryMode;
    @JsonProperty("DistributionMode")
    //配送方式
    private DistributionMode DistributionMode;
    @JsonProperty("SaleOrderDetails")
    //存货编码
    private List<SaleOrderDetails> SaleOrderDetails;
    //包装要求
    //标签要求
    //报告要求
    //技术规格要求
    @JsonProperty("DynamicPropertyKeys")
    private List<String> DynamicPropertyKeys;
    @JsonProperty("DynamicPropertyValues")
    private List<String> DynamicPropertyValues;

    @JsonProperty("ID")
    private Integer ID;

}
