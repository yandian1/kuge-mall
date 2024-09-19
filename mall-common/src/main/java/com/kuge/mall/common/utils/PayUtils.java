package com.kuge.mall.common.utils;

import com.kuge.mall.common.constant.PayStatusEnum;
import java.util.Objects;

/**
 * created by xbxie on 2024/6/2
 */
public class PayUtils {
    public static String getStatusDesc(String code) {

        if (Objects.equals(code, PayStatusEnum.UN_PAY.getCode())) return PayStatusEnum.UN_PAY.getMsg();

        if (Objects.equals(code, PayStatusEnum.PAID.getCode())) return PayStatusEnum.PAID.getMsg();

        if (Objects.equals(code, PayStatusEnum.REFUNDED.getCode())) return PayStatusEnum.REFUNDED.getMsg();

        if (Objects.equals(code, PayStatusEnum.CLOSED.getCode())) return PayStatusEnum.CLOSED.getMsg();

        return "";
    }
}
