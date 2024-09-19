package com.kuge.mall.product.vo;

import lombok.Data;
import java.math.BigDecimal;

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
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * sku划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品首图
     */
    private String firstImg;
}
