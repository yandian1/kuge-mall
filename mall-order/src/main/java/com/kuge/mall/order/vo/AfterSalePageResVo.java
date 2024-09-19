package com.kuge.mall.order.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class AfterSalePageResVo {

    /**
     * 售后id
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 售后状态
     */
    private String status;

    /**
     * 售后状态描述
     */
    private String statusDesc;

    /**
     * 订单商品
     */
    private OrderGoods orderGoods;
}
