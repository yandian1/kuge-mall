package com.kuge.mall.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class RefundReqVo {
    /**
     * 售后 id
     */
    private Long id;

    /**
     * 实际退款金额
     */
    private BigDecimal actualAmount;
}
