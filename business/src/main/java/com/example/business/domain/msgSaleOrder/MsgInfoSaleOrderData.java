package com.example.business.domain.msgSaleOrder;
import com.example.business.domain.SaleOrderOther.Customer;
import com.example.business.domain.SaleOrderOther.DistributionMode;
import com.example.business.domain.SaleOrderOther.SaleOrderDetails;
import com.example.business.domain.SaleOrderOther.Unit;
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
    @JsonProperty("Auditor")
    private String  Auditor;
    @JsonProperty("Address")
    //送货地址
    private String  Address;
    @JsonProperty("linkMan")
    //客户联系人
    private String  linkMan;
    @JsonProperty("CustomerPhone")
    //客户手机号
    private String  CustomerPhone;
    @JsonProperty("DeliveryMode")
    //运输方式
    private String  DeliveryMode;
    @JsonProperty("DistributionMode")
    //配送方式
    private DistributionMode DistributionMode;
    @JsonProperty("SaleOrderDetails")
    //存货编码
    private List<SaleOrderDetails> SaleOrderDetails;

}
