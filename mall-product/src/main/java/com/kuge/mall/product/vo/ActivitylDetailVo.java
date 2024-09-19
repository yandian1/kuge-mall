package com.kuge.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivitylDetailVo {

    private Long id;

    private String name;

    private String banner;

    private List<Section> sections;

    @Data
    public static class Section {

        private String title;

        private List<GoodsItem> goodsList;
    }

    @Data
    public static class GoodsItem {

        private Long id;

        private String name;

        private String intro;

        private String img;

        private BigDecimal price;

        private BigDecimal linePrice;
    }
}
