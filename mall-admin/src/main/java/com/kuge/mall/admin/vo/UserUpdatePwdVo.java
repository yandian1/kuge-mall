package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdatePwdVo {
    /**
     * 用户id
     */
    @NotNull(message = "请输入用户id")
    private Long id;

    /**
     * 旧密码
     */
    @NotBlank(message = "请输入旧密码")
    private String oldPwd;

    /**
     * 新密码
     */
    @NotBlank(message = "请输入新密码")
    private String newPwd;

    /**
     * 确认密码
     */
    @NotBlank(message = "请输入确认密码")
    private String confirmPwd;
}
