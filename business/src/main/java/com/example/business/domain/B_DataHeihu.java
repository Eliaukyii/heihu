package com.example.business.domain;

import lombok.Data;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.util.List;
import java.util.Map;

@Data
public class B_DataHeihu {


    /**
     * 父项物料编号
     */
    private String materialCode;

    /**
     * 成品率（%）
     */
    private String productRate;

    /**
     * 工艺路线
     */
    private String processRoute;

    /**
     * 报工方式
     */
    private Integer[] reportingMethods;

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
    private Integer warehousing;

    /**
     * 自动入库
     */
    private Integer autoWarehousingFlag;

    /**
     * 子项物料编号
     */
    private List<B_DataChildHeihu> bomInputMaterials;

    /**
     * 多产出物料行，仅存报工工序号：workReportProcessNum
     */
    private List<Map<String, Object>> bomOutputMaterials;



}
