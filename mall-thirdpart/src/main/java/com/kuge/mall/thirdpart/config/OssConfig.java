package com.kuge.mall.thirdpart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * created by xbxie on 2024/6/5
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String bucketDomain;
}
