package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SeckillPageItemVo {

    private Long id;

    private String name;

    private String banner;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
