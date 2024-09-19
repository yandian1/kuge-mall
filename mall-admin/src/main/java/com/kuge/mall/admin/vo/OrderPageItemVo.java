package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class OrderPageItemVo {

    private Long id;

    private String sn;

    private String phone;

    private String status;

    private String statusDesc;

    private String payStatus;

    private String payStatusDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<OrderItem> orderItems;

    @Data
    public static class OrderItem {

        private Long id;

        private String status;

        private String statusDesc;

        private String payStatus;

        private String payStatusDesc;

        private String name;

        private String attrs;

        private String img;

        private Integer num;
    }
}
