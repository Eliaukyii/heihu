package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryMode {
    @JsonProperty("Name")
    private String Name;
}
