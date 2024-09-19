package com.kuge.mall.common.utils;

import com.kuge.mall.common.constant.FreightFeeTypeEnum;
import java.util.Objects;

/**
 * created by xbxie on 2024/6/6
 */
public class FreightUtils {
    public static String getTypeDesc(String code) {

        if (Objects.equals(code, FreightFeeTypeEnum.FREE.getCode())) return FreightFeeTypeEnum.FREE.getMsg();

        if (Objects.equals(code, FreightFeeTypeEnum.COUNT.getCode())) return FreightFeeTypeEnum.COUNT.getMsg();

        if (Objects.equals(code, FreightFeeTypeEnum.WEIGHT.getCode())) return FreightFeeTypeEnum.WEIGHT.getMsg();

        return "";
    }
}
