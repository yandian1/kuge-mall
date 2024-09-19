package com.kuge.mall.order.vo;

import lombok.Data;
import java.util.List;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class ConfirmOrderReqVo {

    /**
     * 是否根据购物车创建订单
     */
    private Boolean byCart;

    /**
     * 地址id
     */
    private Long addressId;

    /**
     * sku id
     */
    private Long id;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 使用优惠券的商品集合
     */
    private List<Coupon> couponList;

    @Data
    public static class Coupon {

        /**
         * sku id
         */
        private Long skuId;

        /**
         * 会员优惠券id
         */
        private Long memberCouponId;
    }
}
