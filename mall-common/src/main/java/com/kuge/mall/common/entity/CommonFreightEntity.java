package com.kuge.mall.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Data
@TableName("pms_freight")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonFreightEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 运费模板id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 模板名称
	 */
	private String name;

	/**
	 * 计费类型
	 */
	private String type;

	/**
	 * 首重
	 */
	private Double firstWeight;

	/**
	 * 首重费用
	 */
	private BigDecimal firstWeightFee;

	/**
	 * 续重
	 */
	private Double continueWeight;

	/**
	 * 续重费用
	 */
	private BigDecimal continueWeightFee;

	/**
	 * 首件
	 */
	private Integer firstCount;

	/**
	 * 首件费用
	 */
	private BigDecimal firstCountFee;

	/**
	 * 续件
	 */
	private Integer continueCount;

	/**
	 * 续件费用
	 */
	private BigDecimal continueCountFee;

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
