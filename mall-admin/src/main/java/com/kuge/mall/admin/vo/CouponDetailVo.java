package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponDetailVo {

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
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 抵扣
     */
    private BigDecimal deduct;

    /**
     * 使用门槛
     */
    private BigDecimal threshold;

    /**
     * 用户集合
     */
    private List<Member> memberList;

    /**
     * 商品集合
     */
    private List<Spu> spuList;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime endTime;

    /**
     * 会员范围
     */
    private String memberRange;

    /**
     * 商品范围
     */
    private String goodsRange;

    @Data
    public static class Spu {

        private Long id;

        private String firstImg;

        private String name;

        private BigDecimal price;

        private Integer status;

        private String statusDesc;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }

    @Data
    public static class Member {

        private Long id;

        private String name;

        private String account;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }
}
