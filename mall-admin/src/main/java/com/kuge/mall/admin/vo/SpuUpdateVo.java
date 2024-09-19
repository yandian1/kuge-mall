package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class SpuUpdateVo extends SpuAddVo {
    /**
     * id
     */
    @NotNull(message = "请输入商品id")
    private Long id;
}