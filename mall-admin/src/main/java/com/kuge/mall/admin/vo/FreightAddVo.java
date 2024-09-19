package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FreightAddVo {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 收费方式
     */
    private String type;

    /**
     * 首重
     */
    private Double firstWeight;

    /**
     * 首重费用
     */
    private BigDecimal firstWeightFee;

    /**
     * 续重
     */
    private Double continueWeight;

    /**
     * 续重费用
     */
    private BigDecimal continueWeightFee;

    /**
     * 首件
     */
    private Integer firstCount;

    /**
     * 首件费用
     */
    private BigDecimal firstCountFee;

    /**
     * 续件
     */
    private Integer continueCount;

    /**
     * 续件费用
     */
    private BigDecimal continueCountFee;
}
