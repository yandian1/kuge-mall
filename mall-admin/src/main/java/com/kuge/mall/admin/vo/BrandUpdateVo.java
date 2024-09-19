package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BrandUpdateVo {
    /**
     * id
     */
    @NotNull(message = "请输入id")
    private Long id;

    /**
     * 品牌名称
     */
    @NotBlank(message = "请输入品牌名称")
    private String name;


    /**
     * 品牌图片
     */
    @NotBlank(message = "请选择品牌图片")
    private String img;
}
