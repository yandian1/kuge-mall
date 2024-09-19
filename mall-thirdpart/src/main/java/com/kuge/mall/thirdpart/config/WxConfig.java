package com.kuge.mall.thirdpart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * created by xbxie on 2024/5/31
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WxConfig {
    private String mchId;

    private String mchSerialNo;

    private String privateKeyPath;

    private String apiV3Key;

    private String domain;

    private String notifyDomain;

    private String partnerKey;
}
