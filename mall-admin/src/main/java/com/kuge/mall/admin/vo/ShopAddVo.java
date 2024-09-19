package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class ShopAddVo {
    /**
     * 品牌名称
     */
    @NotBlank(message = "请输入店铺名称")
    private String name;
}