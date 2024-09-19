package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Data
@TableName("pms_brand")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonBrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 品牌名称
	 */
	private String name;

	/**
	 * 品牌图片
	 */
	private String img;

	/**
	 * 品牌详情
	 */
	private String detail;

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
