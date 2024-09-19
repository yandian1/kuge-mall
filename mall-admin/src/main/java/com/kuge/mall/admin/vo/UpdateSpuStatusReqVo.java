package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/6/3
 */
@Data
public class UpdateSpuStatusReqVo {
    /**
     * spuId
     */
    @NotNull(message = "商品id不能为空")
    private Long id;

    /**
     * 商品状态 0：未上架，1：已上架
     */
    @NotNull(message = "商品状态不能为空")
    private Integer status;
}
