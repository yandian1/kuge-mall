package com.kuge.mall.cart.vo;

import lombok.Data;
import java.util.List;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class ChangeSelectReqVo {
    /**
     * 店铺 id
     */
    private Long shopId;

    /**
     * 商品集合
     */
    private List<Goods> goodsList;


    @Data
    public static class Goods {
        /**
         * 购物车id
         */
        private Long skuId;

        /**
         * 商品选择状态 选中或不选中
         */
        private Boolean selected;
    }
}