package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeckillDetailVo {

    private Long id;

    private String name;

    private String banner;

    private List<SeckillDetailVo.Time> timeList;

    @Data
    public static class Time {

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private List<Spu> spuList;
    }

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
}
