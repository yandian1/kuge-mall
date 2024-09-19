package com.kuge.mall.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponUpdateVo extends CouponAddVo {
    /**
     * 优惠券 id
     */
    private Long id;
}
