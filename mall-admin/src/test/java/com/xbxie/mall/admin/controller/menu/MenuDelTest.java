package com.kuge.mall.admin.controller.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.service.CommonMenuService;
import com.kuge.mall.common.utils.R;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试删除菜单接口
 * created by xbxie on 2024/4/28
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试删除菜单接口")
public class MenuDelTest {

    private static final String url = "/menu/del";

    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonMenuService commonMenuService;

    private List<Long> menuIds = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        Random random = new Random();
        // 新增三条测试数据
        for (int i = 0; i < 3; i++) {
            CommonMenuEntity menuEntity = new CommonMenuEntity();
            menuEntity.setId(null);
            menuEntity.setPid(null);
            menuEntity.setName(random.nextLong() + "");
            menuEntity.setPath("/" + i);
            menuEntity.setIsDel(null);
            menuEntity.setCreateTime(null);
            menuEntity.setUpdateTime(null);
            
            commonMenuService.save(menuEntity);
            menuIds.add(menuEntity.getId());
        }
    }

    @AfterEach
    public void afterEach() {
        // 删除测试数据，通过原生sql绕过逻辑删除
        Assertions.assertTrue(testUtils.deleteBatchPhysical("ums_menu", menuIds));
    }

    @DisplayName("删除菜单")
    @Test
    void del() {
        Long menuId = this.menuIds.stream().findAny().get();

        R<Void> resData = testUtils.getResData(url + "/" + menuId, null);

        // 进行返回结果
        Assertions.assertEquals(0, resData.getCode());
        Assertions.assertEquals("删除菜单成功", resData.getMsg());


        // 验证数据库中的数据
        // 验证菜单表
        QueryWrapper<CommonMenuEntity> wrapper = new QueryWrapper<CommonMenuEntity>().eq("id", menuId);
        Assertions.assertEquals(0, commonMenuService.count(wrapper));

    }

    @DisplayName("删除的菜单不存在")
    @Test
    void notExists() {
        long id = new Random().nextLong();
        testUtils.simplePerformAssert(url + "/" + id, null, 500, "菜单不存在");
    }
}
