package com.kuge.mall.order.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class CreateOrderResVo {
    /**
     * 支付二维码链接
     */
    private String codeUrl;

    /**
     * 订单号
     */
    private String sn;
}
