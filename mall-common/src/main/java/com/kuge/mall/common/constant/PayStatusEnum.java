package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum PayStatusEnum {
    UN_PAY("unPay", "未支付"),
    PAID("paid", "已支付"),
    REFUNDED("refunded", "已退款"),
    CLOSED("closed", "已关闭");

    private final String code;

    private final String msg;

    PayStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
