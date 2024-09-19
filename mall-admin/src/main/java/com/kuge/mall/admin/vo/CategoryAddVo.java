package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class CategoryAddVo {
    /**
     * 父分类id
     */
    private Long pid;

    /**
     * 分类名称
     */
    @NotBlank(message = "请输入分类名称")
    private String name;


    /**
     * 分类图片
     */
    private String img;

    /**
     * 排序字段，值越小位置越靠前
     */
    private Integer sort;
}