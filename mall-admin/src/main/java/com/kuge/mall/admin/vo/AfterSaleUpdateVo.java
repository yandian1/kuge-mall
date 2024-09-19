package com.kuge.mall.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class AfterSaleUpdateVo {
    /**
     * 售后id
     */
    private Long id;

    /**
     * 售后状态
     */
    private String status;

    /**
     * 实际退款金额
     */
    private BigDecimal actualAmount;
}
