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
@TableName("sms_activity_section")
public class CommonActivitySectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动分区id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 活动id
	 */
	private Long activityId;

	/**
	 * 活动标题
	 */
	private String title;

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
