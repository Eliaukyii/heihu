package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SaleOrderDetails {
    @JsonProperty("Inventory")
    private Inventory Inventory;
    @JsonProperty("Quantity")
    private String Quantity;
    @JsonProperty("Unit")
    private Unit Unit;
    @JsonProperty("DeliveryDate")
    private String DeliveryDate;
}
