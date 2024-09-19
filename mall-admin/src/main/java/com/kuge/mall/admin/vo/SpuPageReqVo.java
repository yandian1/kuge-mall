package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class SpuPageReqVo {
    /**
     * 商品名称
     */
    private String name;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 商品分类id
     */
    private Long categoryId;

    /**
     * 商品状态 0：未上架，1：已上架
     */
    private Long status;

    /**
     * 当前页码
     */
    @Min(value = 1, message = "pageNum需要大于等于1")
    @NotNull(message = "pageNum不能为空")
    private Long pageNum;

    /**
     * 每页包含的数据条数
     */
    @Min(value = 1, message = "pageSize需要大于等于1")
    @NotNull(message = "pageSize不能为空")
    private Long pageSize;
}
