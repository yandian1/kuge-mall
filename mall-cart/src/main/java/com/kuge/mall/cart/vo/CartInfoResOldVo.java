package com.kuge.mall.cart.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class CartInfoResOldVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalPrice;

    private Boolean totalSelected;

    private Boolean indeterminate;

    private List<ShopItem> shopList;

    @Data
    public static class ShopItem {

        private Long id;

        private String name;

        private Boolean selected;

        private Boolean indeterminate;

        private List<Goods> goodsList;
    }

    @Data
    public static class Goods {

        private Long cartId;

        private Long spuId;

        private Long skuId;

        private String name;

        private String attrs;

        private String img;

        private Integer num;

        private Integer stock;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private BigDecimal price;

        private Boolean selected;
    }
}
