package com.kuge.mall.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * created by xbxie on 2024/5/13
 */
@Data
public class AdminInfoVo {

    /**
     * token
     */
    private String token;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 用户角色
     */
    private List<Long> roleIds;

    /**
     * 用户菜单
     */
    private List<Menu> menus;

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

    @Data
    public static class Menu {
        /**
         * 菜单id
         */
        private Long id;

        /**
         * 菜单名
         */
        private String name;

        /**
         * 菜单路径
         */
        private String path;

        /**
         * 排序字段，值越小位置越靠前
         */
        private Integer sort;

        /**
         * 子菜单
         */
        private List<Menu> children;
    }
}
