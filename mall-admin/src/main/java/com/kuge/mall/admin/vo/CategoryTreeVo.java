package com.kuge.mall.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class CategoryTreeVo {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 父分类id
     */
    private Long pid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 子分类
     */
    private List<CategoryTreeVo> children;
}
