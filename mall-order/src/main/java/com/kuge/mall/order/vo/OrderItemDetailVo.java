package com.kuge.mall.order.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/6/5
 */
@Data
public class OrderItemDetailVo {

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 订单商品
     */
    private OrderGoods orderGoods;
}
