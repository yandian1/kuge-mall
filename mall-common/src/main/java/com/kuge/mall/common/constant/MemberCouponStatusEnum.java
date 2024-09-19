package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum MemberCouponStatusEnum {
    UNUSE("unUse", "待使用"),
    USED("used", "已使用"),
    EXPIRED("expired", "已过期");

    MemberCouponStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
