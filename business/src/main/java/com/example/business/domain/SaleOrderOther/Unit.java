package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Unit {
    @JsonProperty("Code")
    private String Code;
    //销售单位
    @JsonProperty("Name")
    private String Name;
}
