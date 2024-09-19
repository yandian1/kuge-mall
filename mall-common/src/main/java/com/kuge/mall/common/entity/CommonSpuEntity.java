package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@Data
@TableName("pms_spu")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonSpuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * spuId
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 品牌id
	 */
	private Long brandId;

	/**
	 * 商品分类id
	 */
	private Long categoryId;

	/**
	 * 运费模板id
	 */
	private Long freightId;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品价格
	 */
	private BigDecimal price;

	/**
	 * 商品划线格
	 */
	private BigDecimal linePrice;

	/**
	 * 商品首图
	 */
	private String firstImg;

	/**
	 * 商品简介
	 */
	private String intro;

	/**
	 * 商品详情
	 */
	private String detail;

	/**
	 * 商品轮播图
	 */
	private String imgs;

	/**
	 * 支持的服务
	 */
	private String service;

	/**
	 * 商品状态 0：未上架，1：已上架
	 */
	private Integer status;

	/**
	 * 是否删除 0：未删除，1已删除
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
