package com.kuge.mall.admin.controller.menu;

import com.alibaba.fastjson.TypeReference;
import com.kuge.mall.admin.utils.TestUtils;
import com.kuge.mall.admin.vo.MenuPageReqVo;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonMenuService;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 测试菜单分页接口
 * created by xbxie on 2024/4/28
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("测试菜单分页接口")
public class MenuPageTest {
    private static final String url = "/menu/pageList";

    private static final String namePrefix = new Random().nextLong() + "";

    @Resource
    private TestUtils testUtils;

    @Resource
    private CommonMenuService commonMenuService;

    private List<Long> menuIds = new ArrayList<>();

    private List<CommonMenuEntity> menuEntities = new ArrayList<>();
    
    @BeforeEach
    public void beforeEach() {
        // 初始化测试数据

        // 添加菜单
        for (int i = 1; i <= 30; i++) {
            CommonMenuEntity menuEntity = new CommonMenuEntity();

            menuEntity.setId(null);
            menuEntity.setName(namePrefix + i);
            menuEntity.setPath("/" + namePrefix + i);
            menuEntity.setIsDel(0);
            menuEntity.setCreateTime(null);
            menuEntity.setUpdateTime(null);

            commonMenuService.save(menuEntity);
            menuEntities.add(menuEntity);
            menuIds.add(menuEntity.getId());
        }
    }

    @AfterEach
    public void afterEach() {
        // 删除测试数据，通过原生sql绕过逻辑删除
        Assertions.assertTrue(testUtils.deleteBatchPhysical("ums_menu", menuIds));
    }

    @DisplayName("分页数据")
    @Test
    void pageData() {
        MenuPageReqVo menuPageReqVo = new MenuPageReqVo();
        menuPageReqVo.setName(namePrefix + "1"); // 12
        menuPageReqVo.setPageNum(1L);
        menuPageReqVo.setPageSize(10L);
        R<PageData<CommonMenuEntity>> resData = testUtils.getResData(url, menuPageReqVo, new TypeReference<R<PageData<CommonMenuEntity>>>() {});

        Assertions.assertEquals(0, resData.getCode());
        Assertions.assertEquals("success", resData.getMsg());
        Assertions.assertEquals(1, resData.getData().getPageNum());
        Assertions.assertEquals(10, resData.getData().getPageSize());
        Assertions.assertEquals(10, resData.getData().getList().size());
        Assertions.assertEquals(menuEntities.stream().filter(menuEntity -> Pattern.compile(".*" + namePrefix + "1.*").matcher(menuEntity.getName()).matches()).count(), resData.getData().getTotal());

        for (CommonMenuEntity menuEntity : resData.getData().getList()) {
            Assertions.assertTrue(
                Pattern.compile(".*" + namePrefix + "1.*").matcher(menuEntity.getName()).matches()
            );

        }
    }

    @DisplayName("pageNum的值为1000000000，pageSize的值为1000000000")
    @Test
    void pageParamsTooLarge() {
        MenuPageReqVo menuPageReqVo = new MenuPageReqVo();
        menuPageReqVo.setName(namePrefix + "30");
        menuPageReqVo.setPageNum(1000000000L);
        menuPageReqVo.setPageSize(1000000000L);

        R<PageData<CommonUserEntity>> pageDataR = testUtils.getResData(url, menuPageReqVo, new TypeReference<R<PageData<CommonUserEntity>>>(){});
        Assertions.assertEquals(1, pageDataR.getData().getTotal());
        Assertions.assertEquals(0, pageDataR.getData().getList().size());
    }

    @DisplayName("pageNum的值为-1/0，pageSize的值为-1/0")
    @Test
    void pageParamsIllegal() {
        Map<Long, String> pageNumMap = new HashMap<>();
        pageNumMap.put(-1L, "pageNum需要大于等于1");
        pageNumMap.put(0L, "pageNum需要大于等于1");

        Map<Long, String> pageSizeMap = new HashMap<>();
        pageSizeMap.put(-1L, "pageSize需要大于等于1");
        pageSizeMap.put(0L, "pageSize需要大于等于1");

        Map<String, Map<Long, String>> map = new HashMap<>();
        map.put("pageNum", pageNumMap);
        map.put("pageSize", pageSizeMap);

        Random random = new Random();

        for (Map.Entry<String, Map<Long, String>> mapEntry : map.entrySet()) {
            String type = mapEntry.getKey();
            for (Map.Entry<Long, String> entry : mapEntry.getValue().entrySet()) {
                Long params = entry.getKey();
                String message = entry.getValue();
                int code = message.equals("success") ? 0 : 500;

                String name = String.valueOf(random.nextLong());
                Long pageNum = 1L;
                Long pageSize = 10L;

                if (type.equals("pageNum")) {
                    pageNum = params;
                    pageSize = 10L;
                } else if (type.equals("pageSize")) {
                    pageNum = 1L;
                    pageSize = params;
                }

                MenuPageReqVo menuPageReqVo = new MenuPageReqVo();
                menuPageReqVo.setName(name);
                menuPageReqVo.setPageNum(pageNum);
                menuPageReqVo.setPageSize(pageSize);

                testUtils.simplePerformAssert(url, menuPageReqVo, code, message);
            }
        }
    }
}
