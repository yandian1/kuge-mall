package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Data
@TableName("pms_shop")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonShopEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 店铺名称
	 */
	private String name;

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
