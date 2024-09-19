package com.kuge.mall.order.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class AfterSaleAddVo {

    /**
     * 子订单 id
     */
    private Long orderItemId;

    /**
     * 商品数量
     */
    private Integer applyNum;

    /**
     * 售后类型
     */
    private String type;

    /**
     * 售后原因
     */
    private String reason;
}