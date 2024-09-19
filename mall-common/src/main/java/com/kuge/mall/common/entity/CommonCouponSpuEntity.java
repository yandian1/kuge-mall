package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-07-13 04:17:39
 */
@Data
@TableName("sms_coupon_spu")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonCouponSpuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 优惠券id
	 */
	private Long couponId;

	/**
	 * 商品id
	 */
	private Long spuId;

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
