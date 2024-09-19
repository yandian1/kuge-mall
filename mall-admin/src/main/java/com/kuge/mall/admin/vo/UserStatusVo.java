package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/15
 */
@Data
public class UserStatusVo {

    /**
     * 用户id
     */
    @NotNull(message = "请输入用户id")
    private Long id;

    /**
     * 账号状态 0：禁用，1：启用
     */
    @NotNull(message = "请输入用户启用状态")
    private Integer status;

}
