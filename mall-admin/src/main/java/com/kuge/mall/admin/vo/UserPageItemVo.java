package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * created by xbxie on 2024/4/20
 */
@Data
public class UserPageItemVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 账号状态 0：禁用，1：启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
