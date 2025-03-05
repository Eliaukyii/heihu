package com.example.business.domain;

import com.example.business.domain.other.InvLocation;
import com.example.business.domain.other.InventoryClass;
import com.example.business.domain.other.Unit;
import com.example.business.domain.other.Warehouse;
import lombok.Data;

/**
 * 物料定义 ERP
 */
@Data
public class MaterialDefinitionErp {


    /**
     * 存货编码 ->
     */
    private String Code;

    /**
     * 存货名称 ->
     */
    private String Name;

    /**
     * 规格型号 ->
     */
    private String Specification;

    /**
     * 存货分类 ->
     */
    private InventoryClass InventoryClass;

    /**
     * 计量单位 ->
     */
    private Unit Unit;

    /**
     * 批号管理 ->
     */
    private String IsBatch;

    /**
     * 自定义 -> 批次号规则
     */
    private String BatchCodeRule;

    /**
     * 默认仓库 ->
     */
    private Warehouse Warehouse;

    /**
     * 货位 ->
     */
    private InvLocation InvLocation;

    /**
     * 自定义 -> 先进先出
     */
    private String Finfout;

    /**
     * 自定义 -> 管控等级
     */
    private String ControledLevel;

    /**
     * 自定义 -> 先进先出属性
     */
    private String FinfoutAttr;

    /**
     * 安全库存
     */
    private String SafeQuantity;


}
