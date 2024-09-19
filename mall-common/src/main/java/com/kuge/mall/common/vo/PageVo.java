package com.kuge.mall.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * created by xbxie on 2024/4/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {
    /**
     * 当前页码
     */
    @Min(value = 1, message = "pageNum需要大于等于1")
    private Long pageNum;

    /**
     * 每页包含的数据条数
     */
    @Min(value = 1, message = "pageSize需要大于等于1")
    private Long pageSize;
}
