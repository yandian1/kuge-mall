package com.kuge.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.kuge.mall.common", "com.kuge.mall.cart"})
@EnableTransactionManagement
public class MallCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallCartApplication.class, args);
    }

}
