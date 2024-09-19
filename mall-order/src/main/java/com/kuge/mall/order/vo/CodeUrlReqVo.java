package com.kuge.mall.order.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class CodeUrlReqVo {
    /**
     * 批次订单号
     */
    private String batchSn;

    /**
     * 订单号
     */
    private String sn;

    /**
     * 支付方式 1：微信支付、2：支付宝支付
     */
    private Integer payType;
}
