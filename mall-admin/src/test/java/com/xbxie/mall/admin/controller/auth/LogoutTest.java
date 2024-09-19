package com.kuge.mall.admin.controller.auth;

import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.LoginReqVo;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.annotation.Resource;
import java.util.Random;

/**
 * created by xbxie on 2024/5/14
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试退出登录接口")
public class LogoutTest {

    private static final String loginUrl = "/auth/login";

    private static final String logoutUrl = "/auth/logout";


    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @DisplayName("退出登录接口")
    @Test
    void logout() {
        Random random = new Random();
        String name = random.nextLong() + "";
        String account = random.nextLong() + "";
        String password = random.nextLong() + "";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 新增测试数据（添加一个用户）
        CommonUserEntity userEntity = new CommonUserEntity();
        userEntity.setName(name);
        userEntity.setAccount(account);
        userEntity.setPassword(encoder.encode(password));
        commonUserService.save(userEntity);


        // 先进行登录，在 redis 存入 token
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setAccount(account);
        loginReqVo.setPassword(password);
        testUtils.assertSuccess(loginUrl, loginReqVo, "success");

        // 从 redis 中获取 token，并装配到退出登录的请求头中
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = stringRedisTemplate.opsForValue().get(userEntity.getId().toString());
        httpHeaders.add("token", token);
        testUtils.basePerformAssert(logoutUrl, null, httpHeaders);

        // 判断 redis 中是否还有 token
        Assertions.assertNull(stringRedisTemplate.opsForValue().get(userEntity.getId().toString()));

        // 删除测试数据（删除上面新增的用户）
        testUtils.deletePhysical("ums_user", userEntity.getId());
    }
}
