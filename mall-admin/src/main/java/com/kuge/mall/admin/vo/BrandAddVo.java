package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class BrandAddVo {
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