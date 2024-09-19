package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class OrderPageReqVo {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 当前页码
     */
    @Min(value = 1, message = "pageNum需要大于等于1")
    @NotNull(message = "pageNum不能为空")
    private Long pageNum;

    /**
     * 每页包含的数据条数
     */
    @Min(value = 1, message = "pageSize需要大于等于1")
    @NotNull(message = "pageSize不能为空")
    private Long pageSize;
}
