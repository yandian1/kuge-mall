package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class ShopPageReqVo {
    /**
     * 店铺名称
     */
    private String name;

    /**
     * 当前页码
     */
    @Min(value = 1, message = "pageNum需要大于等于1")
    @NotNull(message = "pageNum不能为空")
    private Long pageNum;

    /**
     * 每页包含的数据条数
     */
    @Min(value = 1, message = "pageSize需要大于等于1")
    @NotNull(message = "pageSize不能为空")
    private Long pageSize;
}
