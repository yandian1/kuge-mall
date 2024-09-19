package com.kuge.mall.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@Data
@TableName("pms_sku")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonSkuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 商品spuId
	 */
	private Long spuId;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * sku名称
	 */
	private String name;

	/**
	 * sku价格
	 */
	private BigDecimal price;

	/**
	 * sku划线价
	 */
	private BigDecimal linePrice;

	/**
	 * sku图片
	 */
	private String img;

	/**
	 * sku属性
	 */
	private String attrs;

	/**
	 * sku重量
	 */
	private Double weight;

	/**
	 * sku库存
	 */
	private Integer stock;

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
