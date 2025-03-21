package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Customer {
    @JsonProperty("Code")
    private String Code;
    //客户名称
    @JsonProperty("Name")
    private String Name;
}
