package com.kuge.mall.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * created by xbxie on 2024/7/18
 */
public class PriceUtils {
    public static BigDecimal precision(BigDecimal price) {
        return price.setScale(2, RoundingMode.DOWN);
    }
}
