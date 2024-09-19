package com.kuge.mall.admin.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/14
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试Redis工具")
public class RedisUtilsTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void foo() {
        stringRedisTemplate.opsForValue().set("foo", "整合整合");
        String foo = stringRedisTemplate.opsForValue().get("foo");
        System.out.println(foo);
    }
}
