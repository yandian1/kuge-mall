package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/16
 */
@Data
public class MenuDetailVo {
    /**
     * 菜单id
     */
    private Long id;

    /**
     * 父菜单id集合
     */
    private List<Long> pids;

    /**
     * 菜单名
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
