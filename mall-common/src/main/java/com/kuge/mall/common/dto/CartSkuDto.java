package com.kuge.mall.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024/9/7
 */
@Data
public class CartSkuDto {

    private Long cartId;

    private Long spuId;

    private Long skuId;

    private String name;

    private String attrs;

    private String img;

    private Integer num;

    private Integer stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private Boolean selected;
}
