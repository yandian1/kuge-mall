package com.kuge.mall.product;

import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.service.CommonShopService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MallProductApplicationTests {
    @Resource
    private CommonShopService commonShopService;

    @Test
    void contextLoads() {
        List<String> list = Arrays.asList("aaa".split(","));
        System.out.println("list = " + list);
    }
}
