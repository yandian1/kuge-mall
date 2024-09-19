package com.kuge.mall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * created by xbxie on 2024/5/31
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "order")
public class OrderConfig {
    private long delayTimes;
}
