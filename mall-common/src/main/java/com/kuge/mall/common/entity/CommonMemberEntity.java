package com.kuge.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员实体类
 * created by xbxie on 2024/4/20
 */
@Data
@TableName("ums_member")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonMemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    @TableId(type = IdType.AUTO)
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
     * 账号密码
     */
    private String password;

    /**
     * 是否删除 0：未删除，1：已删除
     */
    @TableLogic
    private Integer isDel;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
