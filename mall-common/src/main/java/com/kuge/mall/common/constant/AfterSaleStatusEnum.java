package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum AfterSaleStatusEnum {
    PENDING("pending", "处理中"),
    AGREED("agreed", "已同意"),
    REJECTED("rejected", "已拒绝"),
    REFUNDING("refunding", "退款中"),
    REFUNDED("refunded", "已退款"),
    CANCELLED("cancelled", "已取消");

    AfterSaleStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
