package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class DictValuePageReqVo {
    /**
     * 字典类型 id
     */
    private Long pid;

    /**
     * 字典值
     */
    private String value;

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