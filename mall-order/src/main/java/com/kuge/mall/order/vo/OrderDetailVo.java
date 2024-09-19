package com.kuge.mall.order.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/6/5
 */
@Data
public class OrderDetailVo {
    private Long id;

    private String shopName;

    private String sn;

    private BigDecimal price;

    private Long surplus;

    private String status;

    private String statusDesc;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private LocalDateTime createTime;

    private List<OrderItem> orderItems;

    @Data
    public static class OrderItem {

        private Long id;

        private Long spuId;

        private String name;

        private String attrs;

        private String img;

        private Integer num;

        private String status;

        private String statusDesc;

        private BigDecimal price;
    }
}
