package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/16
 */
@Data
public class RoleDetailVo {
    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 角色菜单
     */
    private List<Long> menuIds;
}
