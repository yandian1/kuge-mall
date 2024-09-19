package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
@Data
@TableName("ams_dict_value")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonDictValueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 字典值id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 父id
	 */
	private Long pid;

	/**
	 * 字典名
	 */
	private String value;

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
