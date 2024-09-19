package com.kuge.mall.thirdpart.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/7/3
 */
@Data
public class SmsCodeReqVo {
    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String account;
}
