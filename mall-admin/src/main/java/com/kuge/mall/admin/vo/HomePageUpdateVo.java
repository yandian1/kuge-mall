package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class HomePageUpdateVo extends HomePageAddVo {
    /**
     * id
     */
    @NotNull(message = "请输入id")
    private Long id;
}