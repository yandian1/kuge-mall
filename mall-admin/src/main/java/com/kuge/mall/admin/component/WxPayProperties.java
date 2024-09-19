package com.kuge.mall.admin.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * created by xbxie on 2024/6/1
 */
@Data
@Component
@ConfigurationProperties(prefix = "wxpay")
public class WxPayProperties {
    private String mchId;

    private String mchSerialNo;

    private String privateKeyPath;

    private String apiV3Key;

    private String appId;

    private String domain;

    private String notifyDomain;

    private String partnerKey;

    private String refundCallback;
}
