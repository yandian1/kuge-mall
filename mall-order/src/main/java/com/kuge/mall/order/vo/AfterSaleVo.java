package com.kuge.mall.order.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class AfterSaleVo {
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 售后id
     */
    private Long id;

    /**
     * 售后单号
     */
    private String sn;

    /**
     * 申请退货数量
     */
    private Integer applyNum;

    /**
     * 申请退款金额
     */
    private BigDecimal applyAmount;

    /**
     * 实际退款金额
     */
    private BigDecimal actualAmount;

    /**
     * 售后类型
     */
    private String type;

    /**
     * 售后原因
     */
    private String reason;

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