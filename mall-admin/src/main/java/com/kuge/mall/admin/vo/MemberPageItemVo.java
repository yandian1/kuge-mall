package com.kuge.mall.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * created by xbxie on 2024/4/24
 */
@Data
public class MemberPageItemVo {

    /**
     * 会员id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
