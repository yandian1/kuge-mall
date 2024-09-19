package com.kuge.mall.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/7/3
 */
@Data
public class SmsCodeReqDto {
    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String account;
}
