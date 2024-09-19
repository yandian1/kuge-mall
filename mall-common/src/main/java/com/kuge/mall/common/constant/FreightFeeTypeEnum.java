package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum FreightFeeTypeEnum {
    FREE("free", "包邮"),
    WEIGHT("weight", "按重量收费"),
    COUNT("count", "按件数收费");

    FreightFeeTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
