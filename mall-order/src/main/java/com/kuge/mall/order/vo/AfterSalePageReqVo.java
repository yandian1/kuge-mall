package com.kuge.mall.order.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/21
 */
@Data
public class AfterSalePageReqVo {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 审核状态
     */
    private String status;

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
