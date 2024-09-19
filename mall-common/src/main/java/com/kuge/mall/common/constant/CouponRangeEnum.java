package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum CouponRangeEnum {
    MEMBER_ALL("all", "all"),
    GOODS_ALL("all", "all"),
    MEMBER_SPECIFIC("specific", "specific"),
    GOODS_SPECIFIC("specific", "specific");

    CouponRangeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
