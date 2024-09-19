package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CategoryUpdateVo extends CategoryAddVo {
    /**
     * id
     */
    @NotNull(message = "请输入id")
    private Long id;
}
