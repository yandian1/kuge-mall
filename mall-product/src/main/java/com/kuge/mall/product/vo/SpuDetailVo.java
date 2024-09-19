package com.kuge.mall.product.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/5/16
 */
@Data
public class SpuDetailVo {
    /**
     * 商品名称
     */
    private String name;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品划线价格
     */
    private BigDecimal linePrice;

    /**
     * 商品首图
     */
    private String firstImg;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 支付的服务
     */
    private List<String> services;

    /**
     * 商品轮播图
     */
    private List<String> imgList;

    /**
     * 商品属性集合
     */
    private List<Attr> attrs;

    /**
     * 商品sku集合
     */
    private List<Sku> skus;

    @Data
    public static class Attr {
        /**
         * 主键
         */
        private Long id;

        /**
         * 属性名
         */
        private String name;

        /**
         * 子属性集合
         */
        private List<Attr> children;
    }

    @Data
    public static class Sku {
        /**
         * 主键
         */
        private Long id;

        /**
         * sku名称
         */
        private String name;

        /**
         * sku价格
         */
        private BigDecimal price;

        /**
         * sku划线价
         */
        private BigDecimal linePrice;

        /**
         * sku图片
         */
        private String img;

        /**
         * sku属性
         */
        private String attrs;

        /**
         * sku库存
         */
        private Integer stock;
    }
}
