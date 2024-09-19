package com.kuge.mall.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountReqVo {
    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String account;

    /**
     * 验证码
     */
    @NotBlank(message = "请输入验证码")
    private String code;
}
