package com.kuge.mall.member.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * created by xbxie on 2024/5/23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NameReqVo {
    /**
     * 姓名
     */
    @NotBlank(message = "请输入姓名")
    private String name;
}
