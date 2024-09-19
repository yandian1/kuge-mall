package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class SpuPageResVo {
    /**
     * id
     */
    private Long id;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品首图
     */
    private String firstImg;

    /**
     * 商品状态 0：未上架，1：已上架
     */
    private Integer status;

    /**
     * 商品状态描述
     */
    private String statusDesc;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
