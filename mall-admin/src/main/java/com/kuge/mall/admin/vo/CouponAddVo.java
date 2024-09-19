package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponAddVo {

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型
     */
    private String type;

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
     * 用户id集合
     */
    private List<Long> memberIds;

    /**
     * 商品id集合
     */
    private List<Long> spuIds;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime endTime;

    /**
     * 用户范围
     */
    private String memberRange;

    /**
     * 商品范围
     */
    private String goodsRange;
}
