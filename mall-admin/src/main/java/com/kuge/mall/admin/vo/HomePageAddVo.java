package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class HomePageAddVo {
    @NotBlank(message = "首页配置不能为空")
    private String detail;
}