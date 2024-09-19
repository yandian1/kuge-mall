package com.kuge.mall.cart.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class CartAddVo {
    /**
     * skuId
     */
    @NotNull(message = "商品id不能为空")
    private Long skuId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    private Integer quantity;
}
