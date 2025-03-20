package com.example.business.domain;

import com.example.business.domain.other.Inventory;
import lombok.Data;

@Data
public class B_DataChildErp {

    /**
     * 需用数量
     */
    private Integer RequiredQuantity;

    /**
     * 损耗率%
     */
    private String WasteRate;

    /**
     * 生产数量
     */
    private Integer ProduceQuantity;

    /**
     * 物料信息（在此是子物料信息）
     */
    private Inventory inventory;

}
