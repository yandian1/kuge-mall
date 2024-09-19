package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum CouponTypeEnum {
    DISCOUNT("discount", "折扣券"),
    DEDUCT("deduct", "抵扣券");

    CouponTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
