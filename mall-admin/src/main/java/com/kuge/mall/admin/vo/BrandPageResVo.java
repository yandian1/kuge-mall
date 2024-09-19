package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class BrandPageResVo {
    /**
     * id
     */
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌图片
     */
    private String img;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
