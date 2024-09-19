package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-06-01 23:13:13
 */
@Data
@TableName("oms_order_item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonOrderItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 子订单id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	private Long memberId;

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * spu id
	 */
	private Long spuId;

	/**
	 * sku id
	 */
	private Long skuId;

	/**
	 * 会员优惠券id
	 */
	private Long memberCouponId;

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
	 * 订单状态
	 */
	private String status;

	/**
	 * 支付状态
	 */
	private String payStatus;

	/**
	 * 商品总额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 优惠金额
	 */
	private BigDecimal couponAmount;

	/**
	 * 运费金额
	 */
	private BigDecimal deliveryAmount;

	/**
	 * 物流公司
	 */
	private String deliveryCompany;

	/**
	 * 物流单号
	 */
	private String deliverySn;

	/**
	 * 是否删除 0：未删除，1：已删除
	 */
	@TableLogic
	private Integer isDel;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}
