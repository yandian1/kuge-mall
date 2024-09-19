package com.kuge.mall.warm.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * created by xbxie on 2024/8/10
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args)  {
        // 执行缓存预热业务...\
        System.out.println("初始化完成");
    }
}
