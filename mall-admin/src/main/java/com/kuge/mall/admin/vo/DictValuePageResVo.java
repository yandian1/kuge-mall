package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class DictValuePageResVo {

    /**
     * 字典类型 id
     */
    private Long pid;

    /**
     * 字典值 id
     */
    private Long id;

    /**
     * 字典值
     */
    private String value;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}