package com.kuge.mall.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest
@AutoConfigureMockMvc
class MallAdminApplicationTests {
    @Test
    void foo() {
        List<Integer> lista = new ArrayList<>(Arrays.asList(1,2));
        List<Integer> listb = new ArrayList<>();
        listb.addAll(lista);
        System.out.println("listb = " + listb);
    }
}
