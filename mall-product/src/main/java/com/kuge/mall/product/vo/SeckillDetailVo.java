package com.kuge.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/16
 */
@Data
public class SeckillDetailVo {

    private Long id;

    private String name;

    private String banner;

    private List<SeckillDetailVo.Time> timeList;

    @Data
    public static class Time {

        private String type;

        private LocalDateTime startTime;

        private Long surplus;

        private List<Spu> spuList;
    }

    @Data
    public static class Spu {

        private Long id;

        private String name;

        private String intro;

        private String img;

        private BigDecimal price;

        private BigDecimal linePrice;
    }
}
