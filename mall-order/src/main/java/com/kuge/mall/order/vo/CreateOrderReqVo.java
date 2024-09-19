package com.kuge.mall.order.vo;

import lombok.Data;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class CreateOrderReqVo {
    /**
     * 是否根据购物车创建订单
     */
    private Boolean byCart;

    /**
     * 收获地址id
     */
    @NotNull(message = "请输入收获地址")
    private Long addressId;

    /**
     * 店铺列表
     */
    private List<Shop> shopList;

    /**
     * 留言
     */
    private String message;

    @Data
    public static class Shop {
        /**
         * 店铺 id
         */
        private Long id;

        /**
         * 商品列表
         */
        private List<Goods> goodsList;
    }

    @Data
    public static class Goods {
        /**
         * spu id
         */
        @NotNull(message = "请输入商品id")
        private Long spuId;

        /**
         * sku id
         */
        @NotNull(message = "请输入商品id")
        private Long skuId;

        /**
         * 会员优惠券 id
         */
        private Long memberCouponId;

        /**
         * 商品数量
         */
        @Min(value = 1, message = "商品数量至少为1")
        @NotNull(message = "请输入商品商品数量")
        private Integer skuNum;
    }
}
