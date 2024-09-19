package com.kuge.mall.admin.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.refund.RefundService;
import com.kuge.mall.admin.component.WxPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.stream.Collectors;
import com.wechat.pay.java.core.Config;
import org.springframework.core.io.ClassPathResource;

/**
 * created by xbxie on 2024/5/31
 */
@Configuration
public class WxPayConfig {

    @Resource
    private WxPayProperties wxPayProperties;

    @Bean
    public Config config() {
        try {
            ClassPathResource classPathResource = new ClassPathResource(wxPayProperties.getPrivateKeyPath());
            String privateKeyString = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))
                .lines().collect(Collectors.joining(System.lineSeparator()));
            PrivateKey privateKey = PemUtil.loadPrivateKeyFromString(privateKeyString);

            return new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMchId())
                .privateKey(privateKey)
                .merchantSerialNumber(wxPayProperties.getMchSerialNo())
                .apiV3Key(wxPayProperties.getApiV3Key())
                .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RefundService refundService() {
        try {
            ClassPathResource classPathResource = new ClassPathResource(wxPayProperties.getPrivateKeyPath());
            String privateKeyString = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))
                .lines().collect(Collectors.joining(System.lineSeparator()));
            PrivateKey privateKey = PemUtil.loadPrivateKeyFromString(privateKeyString);


            Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMchId())
                .privateKey(privateKey)
                .merchantSerialNumber(wxPayProperties.getMchSerialNo())
                .apiV3Key(wxPayProperties.getApiV3Key())
                .build();

            return new RefundService.Builder().config(config).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
