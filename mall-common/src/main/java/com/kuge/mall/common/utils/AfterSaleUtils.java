package com.kuge.mall.common.utils;

import java.util.Objects;
import com.kuge.mall.common.constant.AfterSaleStatusEnum;

/**
 * created by xbxie on 2024/6/6
 */
public class AfterSaleUtils {
    public static String getStatusDesc(String code) {

        if (Objects.equals(code, AfterSaleStatusEnum.PENDING.getCode())) return AfterSaleStatusEnum.PENDING.getMsg();

        if (Objects.equals(code, AfterSaleStatusEnum.AGREED.getCode())) return AfterSaleStatusEnum.AGREED.getMsg();

        if (Objects.equals(code, AfterSaleStatusEnum.REJECTED.getCode())) return AfterSaleStatusEnum.REJECTED.getMsg();

        if (Objects.equals(code, AfterSaleStatusEnum.REFUNDING.getCode())) return AfterSaleStatusEnum.REFUNDING.getMsg();

        if (Objects.equals(code, AfterSaleStatusEnum.REFUNDED.getCode())) return AfterSaleStatusEnum.REFUNDED.getMsg();

        if (Objects.equals(code, AfterSaleStatusEnum.CANCELLED.getCode())) return AfterSaleStatusEnum.CANCELLED.getMsg();

        return "";
    }
}
