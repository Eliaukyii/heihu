package com.example.business.domain;
import com.example.business.domain.SaleOrderOther.Customer;
import com.example.business.domain.SaleOrderOther.DistributionMode;
import com.example.business.domain.SaleOrderOther.SaleOrderDetails;
import com.example.business.domain.SaleOrderOther.Unit;
import lombok.Data;

@Data
public class SaleOrderErp {
    /**
     * 客户 ->
     */
    private Customer  Customer;

    /**
     * 审核人 ->
     */
    private String  Auditor;

    /**
     * 送货地址 ->
     */
    private String  Address;

    /**
     * 客户联系人 ->
     */
    private String  linkMan;

    /**
     * 客户手机号 ->
     */
    private String  CustomerPhone;

    /**
     * 运输方式 ->
     */
    private String  DeliveryMode;

    /**
     * 配送方式 ->
     */
    private DistributionMode  DistributionMode;

    /**
     * 存货编码 ->
     */
    private SaleOrderDetails  SaleOrderDetails;

    /**
     * 数量 ->
     */
    private String  Quantity;

    /**
     * 销售单位 ->
     */
    private Unit  Unit;

    /**
     * 配送方式 ->
     */
    private String  DeliveryDate;


}
