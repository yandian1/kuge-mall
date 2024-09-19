package com.kuge.mall.auth.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/13
 */
@Data
public class LoginMemberReqVo {
    /**
     * 用户账号
     */
    @NotBlank(message = "请输入账号")
    private String account;

    /**
     * 账号密码
     */
    // @NotBlank(message = "请输入密码")
    // private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "请输入验证码")
    private String code;
}
