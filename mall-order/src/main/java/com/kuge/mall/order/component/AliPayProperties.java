package com.kuge.mall.order.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * created by xbxie on 2024/6/1
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayProperties {
    private String appId;

    private String sellerId;

    private String gatewayUrl;

    private String merchantPrivateKey;

    private String alipayPublicKey;

    private String contentKey;

    private String returnUrl;

    private String notifyUrl;
}
