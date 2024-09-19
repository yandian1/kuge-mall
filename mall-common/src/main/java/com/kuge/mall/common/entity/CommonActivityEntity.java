package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-08-08 04:34:38
 */
@Data
@TableName("sms_activity")
public class CommonActivityEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 活动名
	 */
	private String name;

	/**
	 * 活动 banner
	 */
	private String banner;

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
