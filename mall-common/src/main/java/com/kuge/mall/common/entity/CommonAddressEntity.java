package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Data
@TableName("ums_address")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonAddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 地址id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 会员id
	 */
	private Long userId;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区
	 */
	private String county;

	/**
	 * 省 code
	 */
	private String provinceCode;

	/**
	 * 市 code
	 */
	private String cityCode;

	/**
	 * 区 code
	 */
	private String countyCode;

	/**
	 * 详细地址
	 */
	private String detail;

	/**
	 * 是否为默认地址，1：是，0：否
	 */
	private Integer isDefault;

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
