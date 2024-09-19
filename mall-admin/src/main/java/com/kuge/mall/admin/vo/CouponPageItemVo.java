package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CouponPageItemVo {

    /**
     * 优惠券id
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型
     */
    private String type;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
