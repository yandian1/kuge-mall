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
@TableName("sms_seckill_time")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonSeckillTimeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 秒杀时间段id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 秒杀主键id
	 */
	private Long seckillId;

	/**
	 * 开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 结束时间
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
