package com.kuge.mall.admin.controller.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.MenuAddVo;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.service.impl.CommonMenuServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.assertj.core.util.Arrays;
import javax.annotation.Resource;
import java.util.*;

/**
 * 测试新增菜单接口
 * created by xbxie on 2024/4/25*
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试新增菜单接口")
public class MenuAddTest {
    private static final String url = "/menu/add";

    @Resource
    private TestUtils testUtils;

    @Autowired
    private CommonMenuServiceImpl commonMenuService;

    @DisplayName("插入一个菜单")
    @Test
    void add() {
        MenuAddVo menuAddVo = new MenuAddVo();
        Random random = new Random();

        Long pid = random.nextLong();
        String name = random.nextLong() + "";
        String path = "/" + random.nextLong();

        menuAddVo.setPid(pid);
        menuAddVo.setName(name);
        menuAddVo.setPath(path);

        testUtils.assertSuccess(url, menuAddVo, "添加菜单成功");

        QueryWrapper<CommonMenuEntity> wrapper = new QueryWrapper<CommonMenuEntity>().eq("name", name).eq("url", path);
        CommonMenuEntity menuEntity = commonMenuService.getOne(wrapper);
        Assertions.assertNotNull(menuEntity);
        Assertions.assertTrue(SqlRunner.db().delete("delete from ums_menu where id = " + menuEntity.getId()));

        Assertions.assertNotNull(menuEntity.getId());
        Assertions.assertEquals(menuAddVo.getPid(), menuEntity.getPid());
        Assertions.assertEquals(menuAddVo.getName(), menuEntity.getName());
        Assertions.assertEquals(menuAddVo.getPath(), menuEntity.getPath());
        Assertions.assertEquals(0, menuEntity.getIsDel());
        Assertions.assertNotNull(menuEntity.getCreateTime());
        Assertions.assertNotNull(menuEntity.getUpdateTime());
    }


    @DisplayName("菜单路径重复")
    @Test
    void pathRepeat() {
        Random random = new Random();
        String path1AndPath2 = "/" + random.nextLong();

        MenuAddVo menuAddVo1 = new MenuAddVo();
        menuAddVo1.setName(random.nextLong() + "");
        menuAddVo1.setPath(path1AndPath2);

        MenuAddVo menuAddVo2 = new MenuAddVo();
        menuAddVo2.setName(random.nextLong() + "");
        menuAddVo2.setPath(path1AndPath2);

        testUtils.simplePerformAssert(url, menuAddVo1, 0, "添加菜单成功");
        testUtils.simplePerformAssert(url, menuAddVo2, 500, "菜单路径重复");

        CommonMenuEntity menuEntity = commonMenuService.getOne(new QueryWrapper<CommonMenuEntity>().eq("url", path1AndPath2));
        Assertions.assertNotNull(menuEntity);
        Assertions.assertTrue(SqlRunner.db().delete("delete from ums_menu where id = " + menuEntity.getId()));
    }

    @DisplayName("菜单名重复")
    @Test
    void nameRepeat() {
        Random random = new Random();
        String name1AndName2 = random.nextLong() + "";

        MenuAddVo menuAddVo1 = new MenuAddVo();
        menuAddVo1.setName(name1AndName2);
        menuAddVo1.setPath("/" + random.nextLong());

        MenuAddVo menuAddVo2 = new MenuAddVo();
        menuAddVo2.setName(name1AndName2);
        menuAddVo2.setPath("/" + random.nextLong());

        testUtils.simplePerformAssert(url, menuAddVo1, 0, "添加菜单成功");
        testUtils.simplePerformAssert(url, menuAddVo2, 500, "菜单名重复");

        CommonMenuEntity menuEntity = commonMenuService.getOne(new QueryWrapper<CommonMenuEntity>().eq("name", name1AndName2));
        Assertions.assertNotNull(menuEntity);
        Assertions.assertTrue(SqlRunner.db().delete("delete from ums_menu where id = " + menuEntity.getId()));
    }

    @DisplayName("菜单路径需要以斜杠/开头")
    @Test
    void urlIsBlank() {
        for (String path : Arrays.array("asdf", "asdf/", "asdf/asdf")) {
            MenuAddVo menuAddVo = new MenuAddVo();
            Random random = new Random();
            String name = random.nextLong() + "";
            menuAddVo.setName(name);
            menuAddVo.setPath(path);
            testUtils.assertFail(url, menuAddVo, "菜单路径需要以斜杠/开头");
        }
    }

    @DisplayName("菜单名称/菜单路径非法，值为null/空字符串/包含多个空白字符的空字符串")
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
                List<MenuAddVo> menuAddVos = new ArrayList<>();
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
                    MenuAddVo menuAddVo = new MenuAddVo();
                    menuAddVo.setPid(null);
                    menuAddVo.setName(name);
                    menuAddVo.setPath(url);
                    menuAddVos.add(menuAddVo);
                }

                testUtils.assertFailBatch(url, menuAddVos, message);
            }
        }
    }

    @DisplayName("接口存在")
    @Test
    void exists() {
        MockHttpServletResponse response = testUtils.getResponse(url, null);
        Assertions.assertNotEquals(404, response.getStatus());
    }
}
