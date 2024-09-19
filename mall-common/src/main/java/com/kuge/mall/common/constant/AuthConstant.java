package com.kuge.mall.common.constant;

/**
 * created by xbxie on 2024/5/23
 */
public class AuthConstant {
    /**
     * 认证信息Http请求头
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 管理端 redis token 前缀
     */
    public static final String REDIS_TOKEN_ADMIN_PREFIX = "admin_";

    /**
     * C端 redis token 前缀
     */
    public static final String REDIS_TOKEN_MEMBER_PREFIX = "member_";
}
