package com.kuge.mall.cart.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuge.mall.common.dto.CartSkuDto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class CartInfoResVo {

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

        private List<CartSkuDto> goodsList;
    }
}
