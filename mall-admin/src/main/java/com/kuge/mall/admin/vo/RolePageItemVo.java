package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/25
 */
@Data
public class RolePageItemVo {
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
}
