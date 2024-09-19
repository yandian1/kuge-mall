package com.kuge.mall.sale.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/7/14
 */
@Data
public class CouponPageItemVo {
    /**
     * 优惠券id
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型
     */
    private String type;

    /**
     * 优惠券类型描述
     */
    private String typeDesc;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 抵扣
     */
    private BigDecimal deduct;

    /**
     * 使用门槛
     */
    private BigDecimal threshold;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime endTime;
}
