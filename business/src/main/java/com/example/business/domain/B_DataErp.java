package com.example.business.domain;

import lombok.Data;

import java.util.ArrayList;
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
    private ArrayList<Map<String, Object>> BOMProcessDTOs;

    /**
     * 版本号
     */
    private String version;

    /**
     * 默认BOM
     */
    private String IsCostBOM;

    /**
     * 子件列表？
     */
    private List<B_DataChildErp> BOMChildDTOs;

    /**
     * 生产数量
     */
    private String ProduceQuantity;




}
