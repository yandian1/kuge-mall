package com.kuge.mall.order.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class PayResultReqVo {
    /**
     * 订单号
     */
    private String sn;

    /**
     * 批次订单号
     */
    private String batchSn;
}
