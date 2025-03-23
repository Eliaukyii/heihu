package com.example.business.domain.SaleOrderOther;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomField {
    private String fieldCode;
    private Object fieldValue;
}
