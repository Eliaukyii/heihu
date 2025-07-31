package com.example.business.domain.PurchaseOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Partner {
    //供应商名称
    @JsonProperty("Code")
    private String Code;
}
