package com.kuge.mall.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/25
 */
@Data
public class RoleListItemVo {
    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;
}
