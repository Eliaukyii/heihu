package com.example.business.domain.PurchaseOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldValue2 {
    @JsonProperty("cust_field2__c")
    private String custField2c;
}
