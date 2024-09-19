package com.kuge.mall.common.constant;

import lombok.Getter;

/**
 * created by xbxie on 2024/6/2
 */
@Getter
public enum OrderStatusEnum {
    UN_PAY("unPay", "待付款"),
    UN_SEND("unSend", "待发货"),
    UN_RECEIVE("unReceive", "待收货"),
    RECEIVED("received", "已收货"),
    CANCELLED("cancelled", "已取消"),
    COMPLETED("completed", "完成"),
    AFTER_SALE_ING("afterSaleIng", "售后中"),
    AFTER_SALE_REFUNDED("afterSaleRefunded", "已退款");
    // AFTER_SALE_AGREED("afterSaleAgreed", "售后已退款"),
    // AFTER_SALE_REJECTED("afterSaleRejected", "售后已拒绝"),
    // AFTER_SALE_CANCELLED("afterSaleCancelled", "售后已取消");

    OrderStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;

    private final String msg;

}
