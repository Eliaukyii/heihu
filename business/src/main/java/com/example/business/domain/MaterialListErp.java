package com.example.business.domain;

import lombok.Data;

@Data
public class MaterialListErp {

    /**
     * 父件存货编码
     */
    private String code;

    /**
     * 成品率
     */
    private String YieldRate;

    /**
     * 工艺路线
     */
    private String Routing;

    /**
     * 工序
     */
    private String Process;

    /**
     * 加工方式编码
     * HomeMade：自制
     * OutSource：委外
     */
    private String ProcessMode;

    /**
     * 版本号
     */
    private String Version;

    /**
     * 默认BOM
     */
    private String IsCostBOM;

    /**
     *  -> 子项物料编号
     */
    private String Code_Child;

    /**
     * 生产数量
     */
    private String BOMChildDTOs;

    /**
     * 需用数量
     */
    private BOMChildDTOsBody bomChildDTOsBody;

    /**
     * 损耗率%
     */
    private String WasteRate;

    /**
     * 材料倒冲方式
     */
    private String DrawMaterialModeForDC;


}
@Data
class BOMChildDTOsBody{

    /**
     * 需用数量
     */
    private String RequiredQuantity;

}
