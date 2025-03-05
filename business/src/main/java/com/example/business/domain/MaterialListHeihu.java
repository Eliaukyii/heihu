package com.example.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class MaterialListHeihu {

    /**
     * 父项物料编号
     */
    private String materialCode;

    /**
     * 成品率(%)
     */
    private String productRate;

    /**
     * 工艺路线
     */
    private String processRoute;

    /**
     * 报工工序号
     */
//    private String materialCode;

    /**
     * 报工方式
     */
    private String reportingMethods;

    /**
     * 版本号
     */
    private String version;

    /**
     * 默认版本
     */
    private String defaultVersion;

    /**
     * 是否入库
     */
    private String warehousing;

    /**
     * 自动入库
     */
    private String autoWarehousingFlag;

    /**
     * 子项物料编号
     */
    private List<BomInputMaterials> bomInputMaterials;




}

@Data
class BomInputMaterials {

    /**
     * 子项物料编号
     */
    private String materialCode;

    /**
     * 用量：分子
     */
    private String inputAmountNumerator;

    /**
     * 用量：分母
     */
    private String inputAmountDenominator;

    /**
     * 损耗率
     */
    private String lossRate;

    /**
     * 领料方式
     */
    private String pickMode;

    /**
     * 投料工序号
     */
    private List<BomInputMaterialLines> bomInputMaterialLines;

    /**
     * 投料管控
     */
    private int bomFeedingControls;

}

@Data
class BomInputMaterialLines {

    /**
     * 投料工序号
     */
    private String inputProcessNum;

}
