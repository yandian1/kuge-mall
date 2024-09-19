package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivitylDetailVo {

    private Long id;

    private String name;

    private String banner;

    private List<ActivitylDetailVo.Section> sections;

    @Data
    public static class Section {

        private String title;

        private List<GoodsItem> goodsList;
    }

    @Data
    public static class GoodsItem {

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
