package com.example.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class B_DataChildHeihu {


    /**
     * 项次
     */
    private Integer seq;

    /**
     * 子项物料编号？
     */
    private String materialCode;

    /**
     * 分子
     */
    private String inputAmountNumerator;

    /**
     * 分母
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
     * 指定投料工序
     */
    private Integer specificProcessInput;

    /**
     * 投料工序号
     */
    private String inputProcessNum;

    /**
     * 投料管控
     */
    private B_BomFeedingControlsHeihu bomFeedingControls;


}
