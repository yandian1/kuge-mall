package com.kuge.mall.order.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class OrderPageItemVo {

    private Long id;

    private String sn;

    private String shopName;

    private Long surplus;

    private BigDecimal price;

    private String status;

    private String statusDesc;

    private List<OrderItem> orderItems;

    @Data
    public static class OrderItem {

        private String name;

        private String attrs;

        private String img;

        private Integer num;

        private BigDecimal price;
    }
}
