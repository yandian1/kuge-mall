package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FreightPageItemVo {


    /**
     * 运费模板 id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 收费方式
     */
    private String type;

    /**
     * 收费方式描述
     */
    private String typeDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
