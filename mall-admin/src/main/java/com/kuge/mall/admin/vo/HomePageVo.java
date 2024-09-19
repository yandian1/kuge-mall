package com.kuge.mall.admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class HomePageVo {
    /**
     * id
     */
    private Long id;

    /**
     * 首页配置详情
     */
    private String detail;
}