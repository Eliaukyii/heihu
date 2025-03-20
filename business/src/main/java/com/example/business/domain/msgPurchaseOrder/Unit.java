package com.example.business.domain.msgPurchaseOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Unit {
    @JsonProperty("Name")
    private String Name;
}
