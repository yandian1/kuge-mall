package com.kuge.mall.cart.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class ChangeSelectReqOldVo {
    /**
     * 购物车id
     */
    private Long id;

    /**
     * 商品选择状态 选中或不选中
     */
    private Boolean selected;
}