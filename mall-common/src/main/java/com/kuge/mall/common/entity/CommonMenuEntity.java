package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/25
 */
@Data
@TableName("ams_menu")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单id
     */
    private Long pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 排序字段，值越小位置越靠前
     */
    private Integer sort;

    /**
     * 是否删除 0：未删除，1：已删除
     */
    @TableLogic
    private Integer isDel;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
