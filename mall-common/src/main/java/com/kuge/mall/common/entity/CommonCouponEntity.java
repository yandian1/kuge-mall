package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024-07-13 01:23:16
 */
@Data
@TableName("sms_coupon")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonCouponEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 优惠券id
	 */
	@TableId(type = IdType.AUTO)
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
	 * 会员范围
	 */
	private String memberRange;

	/**
	 * 商品范围
	 */
	private String goodsRange;

	/**
	 * 生效时间
	 */
	private LocalDateTime startTime;

	/**
	 * 过期时间
	 */
	private LocalDateTime endTime;

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
