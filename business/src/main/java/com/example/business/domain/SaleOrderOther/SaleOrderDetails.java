package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SaleOrderDetails {
    @JsonProperty("Inventory")
    private Inventory Inventory;
}
