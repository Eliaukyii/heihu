package com.example.business.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class B_DataErp {

    /**
     * 父件存货编码
     */
    private String Code;

    /**
     * 成品率
     */
    private String YieldRate;

    /**
     * 工艺路线
     */
    private Map<String, Object> Routing;

    /**
     * 工序？
     */
    @JsonProperty("BOMProcessDTOs")
    private List<Map<String, Object>> BOMProcessDTOs;

    /**
     * 版本号
     */
    private String Version;

    /**
     * 默认BOM
     */
    private Boolean IsCostBOM;

    /**
     * 子件列表？
     */
    @JsonProperty("BOMChildDTOs")
    private List<B_DataChildErp> BOMChildDTOs;

    /**
     * 生产数量
     */
    private String ProduceQuantity;


}
