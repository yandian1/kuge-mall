package com.kuge.mall.order.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class PayResultResVo {
    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 订单金额
     */
    private BigDecimal amount;
}
