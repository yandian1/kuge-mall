package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class CategoryPageResVo {
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
     * 分类图片
     */
    private String img;

    /**
     * 排序字段，值越小位置越靠前
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子分类
     */
    private List<CategoryPageResVo> children;
}
