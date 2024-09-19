package com.kuge.mall.product.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/6/5
 */
@Data
public class CategoryVo {

    /**
     * 分类id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图片
     */
    private String img;
}
