package com.kuge.mall.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class MallAuthApplicationTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        String a = "aaa bbb ccc ddd";
        String aaa = a.replace("aaa ", "");
        System.out.println(aaa);
        stringRedisTemplate.opsForValue().set("aaa", aaa);
        System.out.println(stringRedisTemplate.opsForValue().get("aaa"));
    }

}
