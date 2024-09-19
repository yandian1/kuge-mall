package com.kuge.mall.admin.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class DictTypeUpdateVo {
    /**
     * 字典类型 id
     */
    private Long id;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 类型 code
     */
    private String code;
}