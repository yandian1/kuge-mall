package com.kuge.mall.thirdpart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;

/**
 * created by xbxie on 2024/7/3
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String sign;

    private String template;

    @Bean
    public Client client() throws Exception {
        Config config = new Config()
            .setAccessKeyId(accessKeyId)
            .setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;
        return new Client(config);
    }
}
