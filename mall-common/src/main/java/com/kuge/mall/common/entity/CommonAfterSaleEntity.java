package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-06-07 16:15:20
 */
@Data
@TableName("oms_after_sale")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonAfterSaleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 售后 id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 会员 id
	 */
	private Long userId;

	/**
	 * 订单 id
	 */
	private Long orderId;

	/**
	 * 子订单 id
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
	 * 商品数量
	 */
	private Integer applyNum;

	/**
	 * 申请退款金额
	 */
	private BigDecimal applyAmount;

	/**
	 * 实际退款金额
	 */
	private BigDecimal actualAmount;

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
