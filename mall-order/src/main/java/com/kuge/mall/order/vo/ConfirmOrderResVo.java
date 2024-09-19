package com.kuge.mall.order.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class ConfirmOrderResVo {

    private BigDecimal actualPrice;

    private BigDecimal goodsPrice;

    private BigDecimal couponPrice;

    private BigDecimal deliveryPrice;

    private List<ShopItem> shopList;

    private Address address;

    @Data
    public static class Address {

        /**
         * 地址id
         */
        private Long id;

        /**
         * 姓名
         */
        private String name;

        /**
         * 电话
         */
        private String phone;

        /**
         * 省 name
         */
        private String province;

        /**
         * 市 name
         */
        private String city;

        /**
         * 区 name
         */
        private String county;

        /**
         * 详细地址
         */
        private String detail;

        /**
         * 是否为默认地址
         */
        private Boolean isDefault;
    }

    @Data
    public static class ShopItem {

        private Long id;

        private String name;

        private List<Goods> goodsList;
    }

    @Data
    public static class Goods {

        private Long spuId;

        private Long skuId;

        private Long memberCouponId;

        private String name;

        private String attrs;

        private String img;

        private Integer num;

        private BigDecimal price;

        private BigDecimal couponPrice;
        private List<Coupon> couponList;
    }

    @Data
    public static class Coupon {

        /**
         * 会员优惠券id
         */
        private Long memberCouponId;

        /**
         * 优惠券id
         */
        private Long couponId;

        /**
         * 优惠券名称
         */
        private String name;

        /**
         * 优惠券类型
         */
        private String type;

        /**
         * 优惠券类型描述
         */
        private String typeDesc;

        /**
         * 折扣
         */
        private BigDecimal discount;

        /**
         * 抵扣
         */
        private BigDecimal deduct;

        /**
         * 使用门槛
         */
        private BigDecimal threshold;

        /**
         * 生效时间
         */
        private LocalDateTime startTime;

        /**
         * 过期时间
         */
        private LocalDateTime endTime;
    }
}
