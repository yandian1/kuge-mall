package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/6/5
 */
@Data
public class OrderDetailVo {

    private Long id;

    private String sn;

    private String shopName;

    private BigDecimal price;

    private String status;

    private String statusDesc;

    private String payStatus;

    private String payStatusDesc;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private List<OrderGoods> orderGoodsList;

    @Data
    public static class OrderGoods {

        private Long id;

        private Integer num;

        private String attrs;

        private String name;

        private String img;

        private BigDecimal price;

        private String status;

        private String statusDesc;

        private String payStatus;

        private String payStatusDesc;

        private String deliveryCompany;

        private String deliverySn;
    }
}
