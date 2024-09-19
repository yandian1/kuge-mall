package com.kuge.mall.admin.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class DictValueAddVo {
    /**
     * 字典类型 id
     */
    private Long pid;

    /**
     * 字典值
     */
    private String value;
}