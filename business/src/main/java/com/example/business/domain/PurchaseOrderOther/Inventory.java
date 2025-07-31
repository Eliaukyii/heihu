package com.example.business.domain.PurchaseOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Inventory {
    @JsonProperty("Code")
    private String Code;
}
