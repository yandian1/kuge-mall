package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024-06-01 23:13:13
 */
@Data
@TableName("oms_order")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	private Long memberId;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 订单号
	 */
	private String sn;

	/**
	 * 批次订单号
	 */
	private String batchSn;

	/**
	 * 订单状态
	 */
	private String status;

	/**
	 * 支付状态
	 */
	private String payStatus;

	/**
	 * 支付方式 1：微信支付、2：支付宝支付
	 */
	private Integer payType;

	/**
	 * 是否批量支付 0：否、1：是
	 */
	private Integer batchPay;

	/**
	 * 收货人姓名
	 */
	private String receiverName;

	/**
	 * 收货人手机
	 */
	private String receiverPhone;

	/**
	 * 收货人地址
	 */
	private String receiverAddress;

	/**
	 * 留言
	 */
	private String message;

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
