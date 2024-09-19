package com.kuge.mall.admin.controller.menu;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.admin.service.UserService;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.UserAddVo;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonMenuService;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.R;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by xbxie on 2024/5/16
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试获取菜单详情接口")
public class MenuDetailTest {
    private final String url = "/menu/";

    @Resource
    private TestUtils testUtils;

    @Resource
    private UserService userService;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private CommonMenuService commonMenuService;

    

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private List<Long> userIds = new ArrayList<>();

    private List<Long> menuIds = new ArrayList<>();

    private HttpHeaders httpHeaders = new HttpHeaders();


    @BeforeEach
    public void beforeEach() {
        // 初始化测试数据
        Random random = new Random();

        // 添加用户
        String password = random.nextLong() + "";
        UserAddVo userAddVo = new UserAddVo();
        userAddVo.setName(random.nextLong() + "");
        userAddVo.setAccount(random.nextLong() + "");
        userAddVo.setPassword(password);
        userAddVo.setStatus(1);
        userAddVo.setRoleIdList(null);

        userService.add(userAddVo);
        
        CommonUserEntity userEntity = commonUserService.getOne(new QueryWrapper<CommonUserEntity>().eq("name", userAddVo.getName()).eq("account", userAddVo.getAccount()));
        userIds.add(userEntity.getId());

        // 登录获取 token
        // LoginReqVo loginReqVo = new LoginReqVo();
        // loginReqVo.setAccount(userAddVo.getAccount());
        // loginReqVo.setPassword(password);
        // R<LoginResVo> loginResVo = authService.login(loginReqVo);
        // httpHeaders.add("token", loginResVo.getData().getToken());

        // 添加菜单
        CommonMenuEntity menuEntity = new CommonMenuEntity();
        menuEntity.setName(random.nextLong() + "");
        commonMenuService.save(menuEntity);
        menuIds.add(menuEntity.getId());
    }

    @AfterEach
    public void afterEach() {
        // 删除测试数据，通过原生sql绕过逻辑删除
        Assertions.assertTrue(testUtils.deleteBatchPhysical("ums_user", userIds));
        Assertions.assertTrue(testUtils.deleteBatchPhysical("ums_menu", menuIds));
        for (Long userId : userIds) {
            stringRedisTemplate.delete(userId.toString());
        }
    }

    @DisplayName("获取菜单详情")
    @Test
    void getRole() {
        R<CommonMenuEntity> resData = testUtils.getResData(url + this.menuIds.get(0), null, httpHeaders, new TypeReference<R<CommonMenuEntity>>() {
        });
        Assertions.assertNotNull(resData.getData());
        Assertions.assertNotNull(resData.getData().getId());
    }

    @DisplayName("菜单不存在")
    @Test
    void notExists() {
        Random random = new Random();
        testUtils.assertFail(url + random.nextLong(), null, httpHeaders, "菜单不存在");
    }
}
