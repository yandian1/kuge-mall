package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
@Data
@TableName("ums_cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonCartEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 购物车id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 商品数量
	 */
	private Integer quantity;

	/**
	 * 是否选中 0：未选中，1：已选中
	 */
	private Integer selected;

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
