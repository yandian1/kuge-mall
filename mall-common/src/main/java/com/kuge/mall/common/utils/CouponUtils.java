package com.kuge.mall.common.utils;

import com.kuge.mall.common.constant.CouponTypeEnum;
import java.util.Objects;

/**
 * created by xbxie on 2024/7/18
 */
public class CouponUtils {
    public static String getTypeDesc(String code) {

        if (Objects.equals(code, CouponTypeEnum.DISCOUNT.getCode())) return CouponTypeEnum.DISCOUNT.getMsg();

        if (Objects.equals(code, CouponTypeEnum.DEDUCT.getCode())) return CouponTypeEnum.DEDUCT.getMsg();

        return "";
    }
}
