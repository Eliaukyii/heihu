package com.example.business.domain.SaleOrderOther;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Clerk {
    @JsonProperty("Code")
    private String Code;

    @JsonProperty("Name")
    private String Name;
} 