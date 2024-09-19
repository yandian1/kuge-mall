package com.kuge.mall.cart.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class ChangeQuantityReqOldVo {
    /**
     * 购物车 id
     */
    @NotNull(message = "请输入购物车id")
    private Long id;

    /**
     * 商品数量
     */
    @NotNull(message = "请输入商品数量")
    @Min(value = 1, message = "商品数量至少为1")
    private Integer quantity;
}
