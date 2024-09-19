package com.kuge.mall.admin.controller.menu;

import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.service.CommonMenuService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;
import java.util.List;

/**
 * 测试更新菜单接口
 * created by xbxie on 2024/4/25
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试更新菜单接口")
public class MenuUpdateTest {
    private static final String url = "/menu/update";

    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonMenuService commonMenuService;

    private List<Long> menuIds = new ArrayList<>();

    private List<CommonMenuEntity> menuEntities = new ArrayList<>();

    public MenuUpdateVo getNewMenuUpdateVo() {
        Random random = new Random();

        MenuUpdateVo menuUpdateVo = new MenuUpdateVo();
        menuUpdateVo.setId(random.nextLong());
        menuUpdateVo.setPid(null);
        menuUpdateVo.setName(random.nextLong() + "");
        menuUpdateVo.setPath("/" + random.nextLong());

        return menuUpdateVo;
    }

    @BeforeEach
    public void beforeEach() {
        // 初始化测试数据
        // 添加用户
        for (int i = 1; i <= 30; i++) {
            CommonMenuEntity menuEntity = new CommonMenuEntity();

            menuEntity.setId(null);
            menuEntity.setPid(null);
            menuEntity.setName(i + "");
            menuEntity.setPath("/" + i);
            menuEntity.setIsDel(0);
            menuEntity.setCreateTime(null);
            menuEntity.setUpdateTime(null);

            commonMenuService.save(menuEntity);
            CommonMenuEntity menuEntity1 = commonMenuService.getById(menuEntity.getId());
            menuEntities.add(menuEntity1);
            menuIds.add(menuEntity1.getId());
        }
    }

    @AfterEach
    public void afterEach() {
        // 删除测试数据，通过原生sql绕过逻辑删除
        Assertions.assertTrue(testUtils.deleteBatchPhysical("ums_menu", menuIds));
    }

    @DisplayName("更新菜单")
    @Test
    void update() throws Exception {
        CommonMenuEntity menuEntity1Before = this.menuEntities.get(0);
        CommonMenuEntity menuEntity2Before = this.menuEntities.get(1);

        MenuUpdateVo menuUpdateVo = new MenuUpdateVo();
        MenuUpdateVo newMenuUpdateVo = getNewMenuUpdateVo();
        menuUpdateVo.setId(menuEntity1Before.getId());
        menuUpdateVo.setPid(menuEntity2Before.getId());
        menuUpdateVo.setName(newMenuUpdateVo.getName());
        menuUpdateVo.setPath(newMenuUpdateVo.getPath());

        Thread.sleep(1000);
        testUtils.assertSuccess(url, menuUpdateVo,"更新菜单成功");

        CommonMenuEntity menuEntity1After = commonMenuService.getById(menuUpdateVo.getId());
        // id
        Assertions.assertEquals(menuUpdateVo.getId(), menuEntity1After.getId());
        // pid
        Assertions.assertEquals(menuUpdateVo.getPid(), menuEntity1After.getPid());
        // name
        Assertions.assertEquals(menuUpdateVo.getName(), menuEntity1After.getName());
        // url
        Assertions.assertEquals(menuUpdateVo.getPath(), menuEntity1After.getPath());
        // idDel
        Assertions.assertEquals(menuEntity1Before.getIsDel(), menuEntity1After.getIsDel());
        // createTime
        Assertions.assertEquals(menuEntity1Before.getCreateTime(), menuEntity1After.getCreateTime());
        // updateTime
        Assertions.assertNotEquals(menuEntity1Before.getUpdateTime(), menuEntity1After.getUpdateTime());
    }

    @DisplayName("菜单路径重复")
    @Test
    void pathRepeat() {
        CommonMenuEntity menuEntity1 = this.menuEntities.get(0);
        CommonMenuEntity menuEntity2 = this.menuEntities.get(1);
        MenuUpdateVo menuUpdateVo = new MenuUpdateVo();
        BeanUtils.copyProperties(menuEntity1, menuUpdateVo);
        menuUpdateVo.setPath(menuEntity2.getPath());

        testUtils.simplePerformAssert(url, menuUpdateVo, 500, "菜单路径重复");
    }

    @DisplayName("菜单名重复")
    @Test
    void nameRepeat() {
        CommonMenuEntity menuEntity1 = this.menuEntities.get(0);
        CommonMenuEntity menuEntity2 = this.menuEntities.get(1);
        MenuUpdateVo menuUpdateVo = new MenuUpdateVo();
        BeanUtils.copyProperties(menuEntity1, menuUpdateVo);
        menuUpdateVo.setName(menuEntity2.getName());

        testUtils.simplePerformAssert(url, menuUpdateVo, 500, "菜单名重复");
    }

    @DisplayName("更新的菜单不存在")
    @Test
    void notExists() {
        testUtils.simplePerformAssert(url, getNewMenuUpdateVo(), 500, "菜单不存在");
    }

    @DisplayName("菜单名/菜单路径非法，值为null/空字符串/包含多个空白字符的空字符串")
    @Test
    void nameUrlIsBlank() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "请输入菜单名称");
        map.put("url", "请输入菜单路径");

        Random random = new Random();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String message = entry.getValue();
            {
                List<MenuUpdateVo> menuUpdateVos = new ArrayList<>();
                String[] arr = Arrays.array(null, "", "  ");

                for (String s : arr) {
                    String name = "";
                    String url = "";
                    {
                        switch (key) {
                            case "name":
                                name = s;
                                url = "/" + random.nextLong();
                                break;
                            case "url":
                                name = random.nextLong() + "";
                                url = s;
                                break;
                        }
                    }
                    MenuUpdateVo menuUpdateVo = new MenuUpdateVo();
                    menuUpdateVo.setId(this.menuIds.get(0));
                    menuUpdateVo.setPid(null);
                    menuUpdateVo.setName(name);
                    menuUpdateVo.setPath(url);
                    menuUpdateVos.add(menuUpdateVo);
                }

                testUtils.assertFailBatch(url, menuUpdateVos, message);
            }
        }
    }

    @DisplayName("id为null")
    @Test
    void idIsNull() {
        MenuUpdateVo menuUpdateVo = getNewMenuUpdateVo();
        menuUpdateVo.setId(null);
        testUtils.simplePerformAssert(url, menuUpdateVo, 500, "请输入菜单id");
    }
}
