package com.kuge.mall.admin.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class OrderSendGoodsReqVo {

    /**
     * 子订单 id
     */
    private Long id;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliverySn;
}
