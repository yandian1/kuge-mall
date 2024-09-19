package com.kuge.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * created by xbxie on 2024/8/6
 */
@Data
public class OrderGoods {
    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品属性
     */
    private String attrs;

    /**
     * 商品图片
     */
    private String img;
}
