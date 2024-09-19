package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * created by xbxie on 2024/4/25
 */
@Data
public class MenuAddVo {
    /**
     * 父菜单id
     */
    private Long pid;

    /**
     * 菜单名称
     */
    @NotBlank(message = "请输入菜单名称")
    private String name;

    /**
     * 排序字段，值越小位置越靠前
     */
    private Integer sort;

    /**
     * 菜单路径
     */
    @Pattern(regexp = "^(\\s*)|(/.*)$", message = "菜单路径需要以斜杠/开头")
    @NotBlank(message = "请输入菜单路径")
    private String path;
}

//