package com.kuge.mall.auth.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/13
 */
@Data
public class LoginMemberResVo {
    /**
     * token
     */
    private String token;

    /**
     * 购物车商品数量
     */
    private Integer cartCount;

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
