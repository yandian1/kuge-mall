package com.kuge.mall.admin.config;

import com.alibaba.fastjson.TypeReference;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.LoginReqVo;
import com.kuge.mall.admin.vo.LoginResVo;
import com.kuge.mall.admin.vo.UserAddVo;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.R;
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
@DisplayName("测试 Security 配置类")
public class SecurityConfigTest {
    private static final String permitUrl = "/auth/login";

    private static final String authUrl = "/user/add";

    private static final String loginUrl = "/auth/login";


    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public UserAddVo getNewUserAddVo() {
        Random random = new Random();
        UserAddVo userAddVo = new UserAddVo();
        userAddVo.setName(random.nextLong() + "");
        userAddVo.setAccount(random.nextLong() + "");
        userAddVo.setPassword(random.nextLong() + "");
        userAddVo.setStatus(null);
        userAddVo.setRoleIdList(java.util.Arrays.asList(random.nextLong(), random.nextLong(), random.nextLong()));
        return userAddVo;
    }

    @DisplayName("需要认证的接口-有token")
    @Test
    void auth1() {
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

        // 登录
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setAccount(account);
        loginReqVo.setPassword(password);
        testUtils.assertSuccess(loginUrl, loginReqVo, "success");

        // 发送携带 token 的请求
        String token = stringRedisTemplate.opsForValue().get(userEntity.getId().toString());
        UserAddVo userAddVo = getNewUserAddVo();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        testUtils.simplePerformAssert(authUrl, userAddVo, httpHeaders,0, "添加用户成功");

        // 删除测试数据（删除上面新增的用户）
        testUtils.deletePhysical("ums_user", userEntity.getId());
        stringRedisTemplate.delete(userEntity.getId().toString());
    }


    @DisplayName("需要认证的接口-没有token")
    @Test
    void auth2() {
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

        // 登录
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setAccount(account);
        loginReqVo.setPassword(password);
        testUtils.assertSuccess(loginUrl, loginReqVo, "success");

        // 发送携带 token 的请求
        UserAddVo userAddVo = getNewUserAddVo();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", "");
        testUtils.simplePerformAssert(authUrl, userAddVo, httpHeaders,0, "添加用户成功");

        // 删除测试数据（删除上面新增的用户）
        testUtils.deletePhysical("ums_user", userEntity.getId());
        stringRedisTemplate.delete(userEntity.getId().toString());
    }

    @DisplayName("无需认证的接口-有token")
    @Test
    void permit1() {

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

        // 验证 token 的生成
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setAccount(account);
        loginReqVo.setPassword(password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", "tokenvalue");

        R<LoginResVo> resData = testUtils.getResData(permitUrl, loginReqVo, httpHeaders, new TypeReference<R<LoginResVo>>() {});

        Assertions.assertEquals(0, resData.getCode());
        Assertions.assertEquals("success", resData.getMsg());
        Assertions.assertNotNull(resData.getData().getToken());

        // 验证 token 缓存是否存在
        String token = stringRedisTemplate.opsForValue().get(userEntity.getId().toString());
        Assertions.assertNotNull(token);


        // 删除测试数据（删除上面新增的用户）
        testUtils.deletePhysical("ums_user", userEntity.getId());
        stringRedisTemplate.delete(userEntity.getId().toString());
    }

    @DisplayName("无需认证的接口-没有token")
    @Test
    void permit2() {

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

        // 验证 token 的生成
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setAccount(account);
        loginReqVo.setPassword(password);

        R<LoginResVo> resData = testUtils.getResData(permitUrl, loginReqVo, new TypeReference<R<LoginResVo>>() {});

        Assertions.assertEquals(0, resData.getCode());
        Assertions.assertEquals("success", resData.getMsg());
        Assertions.assertNotNull(resData.getData().getToken());

        // 验证 token 缓存是否存在
        String token = stringRedisTemplate.opsForValue().get(userEntity.getId().toString());
        Assertions.assertNotNull(token);


        // 删除测试数据（删除上面新增的用户）
        testUtils.deletePhysical("ums_user", userEntity.getId());
        stringRedisTemplate.delete(userEntity.getId().toString());
    }
}
