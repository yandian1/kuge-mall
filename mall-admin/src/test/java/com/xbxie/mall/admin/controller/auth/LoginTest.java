package com.kuge.mall.admin.controller.auth;

import com.alibaba.fastjson.TypeReference;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.LoginReqVo;
import com.kuge.mall.admin.vo.LoginResVo;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.R;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.annotation.Resource;
import java.util.*;

/**
 * created by xbxie on 2024/5/14
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试登录接口")
public class LoginTest {
    private static final String url = "/auth/login";

    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void foo() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }


    @DisplayName("登录接口")
    @Test
    void login() {
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

        R<LoginResVo> resData = testUtils.getResData(url, loginReqVo, new TypeReference<R<LoginResVo>>() {});

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


    @DisplayName("用户账号/账号密码非法，值为null/空字符串/包含多个空白字符的空字符串")
    @Test
    void accountPasswordIsBlank() {
        Map<String, String> map = new HashMap<>();
        map.put("account", "请输入用户账号");
        map.put("password", "请输入账号密码");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String message = entry.getValue();
            {
                List<LoginReqVo> loginReqVos = new ArrayList<>();
                String[] arr = Arrays.array(null, "", "  ");

                for (int i = 0; i < arr.length; i++) {
                    String account = "";
                    String password = "";
                    {
                        switch (key) {
                            case "account":
                                account = arr[i];
                                password = String.valueOf(System.currentTimeMillis());
                                break;
                            case "password":
                                account = String.valueOf(System.currentTimeMillis());
                                password = arr[i];
                                break;
                        }
                    }


                    LoginReqVo loginReqVo = new LoginReqVo();
                    loginReqVo.setAccount(account);
                    loginReqVo.setPassword(password);
                    loginReqVos.add(loginReqVo);
                }

                testUtils.assertFailBatch(url, loginReqVos, message);
            }
        }
    }
}
