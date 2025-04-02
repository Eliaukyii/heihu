package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldValue {
    @JsonProperty("cust_field2__c")
    private String custField2C;
}
