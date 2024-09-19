package com.kuge.mall.common.utils;

import lombok.Data;

/**
 * created by xbxie on 2024/4/20
 */
@Data
public class CustomException extends RuntimeException {
    int code;
    String message;

    public CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(String message) {
        this(500, message);
    }

    // /**
    //  * Throwable中的fillInStackTrace方法使用synchronized修饰，为了避免其在大量创建CustomException对象时影响系统性能，所以这个方法重写为一个简单的普通方法
    //  */
    // @Override
    // public Throwable fillInStackTrace() {
    //     return this;
    // }
}
