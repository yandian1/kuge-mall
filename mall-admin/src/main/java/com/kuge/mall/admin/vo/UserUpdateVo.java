package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserUpdateVo {
    /**
     * 用户id
     */
    @NotNull(message = "请输入用户id")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "请输入用户名")
    private String name;

    /**
     * 用户账号
     */
    @NotBlank(message = "请输入用户账号")
    private String account;

    /**
     * 账号状态 0：禁用，1：启用
     */
    private Integer status;

    /**
     * 用户角色id集合
     */
    private List<Long> roleIds;
}
