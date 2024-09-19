package com.kuge.mall.cart.vo;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class ChangeQuantityReqVo {
    /**
     * 店铺 id
     */
    @NotNull(message = "请输入shopId")
    private Long shopId;

    /**
     * 商品 id
     */
    @NotNull(message = "请输入skuId")
    private Long skuId;

    /**
     * 商品数量
     */
    @NotNull(message = "请输入商品数量")
    @Min(value = 1, message = "商品数量至少为1")
    private Integer quantity;
}
