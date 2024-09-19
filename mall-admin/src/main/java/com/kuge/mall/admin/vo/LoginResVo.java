package com.kuge.mall.admin.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/13
 */
@Data
public class LoginResVo {
    /**
     * token
     */
    private String token;

    /**
     * 用户信息
     */
    private User user;

    @Data
    public static class User {
        /**
         * 用户id
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
    }
}
