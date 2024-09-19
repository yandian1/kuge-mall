package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class SpuAddVo {
    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 商品分类id
     */
    private Long categoryId;

    /**
     * 运费模板id
     */
    private Long freightId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品首图
     */
    private String firstImg;

    /**
     * 商品简介
     */
    private String intro;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 支付的服务的 id
     */
    private List<Long> serviceIds;

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
         * 属性名称
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
         * sku价格
         */
        private BigDecimal price;

        /**
         * sku划线价
         */
        private BigDecimal linePrice;

        /**
         * sku属性
         */
        private String attrs;

        /**
         * sku重量
         */
        private Double weight;

        /**
         * sku库存
         */
        private Integer stock;
    }
}