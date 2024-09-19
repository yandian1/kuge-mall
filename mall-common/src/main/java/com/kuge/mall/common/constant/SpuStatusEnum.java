package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum SpuStatusEnum {
    SHELF_UP(1, "上架"),
    SHELF_DOWN(0, "下架");

    private final int code;

    private final String msg;

    SpuStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
