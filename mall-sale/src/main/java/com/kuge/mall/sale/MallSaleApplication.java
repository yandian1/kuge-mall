package com.kuge.mall.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kuge.mall.common", "com.kuge.mall.sale"})
public class MallSaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallSaleApplication.class, args);
    }

}
