package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class AfterSalePageResVo {
    /**
     * 售后id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 子订单id
     */
    private Long orderItemId;

    /**
     * 售后订单号
     */
    private String sn;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品数量
     */
    private Integer skuNum;

    /**
     * 商品单价
     */
    private BigDecimal skuPrice;

    /**
     * 商品属性
     */
    private String skuAttrs;

    /**
     * 商品图片
     */
    private String skuImg;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 售后类型 退款、退货退款
     */
    private String type;

    /**
     * 售后原因
     */
    private String reason;

    /**
     * 售后状态
     */
    private String status;

    /**
     * 售后状态描述
     */
    private String statusDesc;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
