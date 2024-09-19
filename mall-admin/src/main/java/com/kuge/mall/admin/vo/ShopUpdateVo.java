package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ShopUpdateVo extends ShopAddVo {
    /**
     * id
     */
    @NotNull(message = "请输入店铺id")
    private Long id;
}
