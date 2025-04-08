package com.example.business.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MaterialDefinitionHeihu {

    /**
     * 物料代码 ->
     */
    private String materialCode;

    /**
     * 物料名称 ->
     */
    private String materialName;

    /**
     * 物料规格 ->
     */
    private String specification;

    /**
     * 物料分类 ->
     */
    private String materialCategoryCode;

    /**
     * 主单位 ->
     */
    private String unitName;

    /**
     * 启用批次管理 ->
     */
    private String batchManagementEnable;

    /**
     * 批次号规则 ->
     */
    private String batchNoRuleCode;

    /**
     * 默认仓库 ->
     */
    private String defaultWarehouseCode;

    /**
     * 默认仓位 ->
     */
    private String defaultLocationCode;

    /**
     * 先进先出 ->
     */
    private String fifoEnabled;

    /**
     * 管控等级 ->
     */
    private String controlLevel;

    /**
     * 先进先出属性 ->
     */
    private String fifoAttr;

    /**
     * 安全库存、平均成本
     */
    private List<Map<String, Object>> customFields;

    /**
     * 状态
     */
    private String enableFlag;



}
