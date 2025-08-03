package com.example.business.domain.PurchaseOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldValue2 {
    @JsonProperty("custField2c")
    private String custField2c;
}
