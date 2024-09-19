package com.kuge.mall.order.config;

import com.alipay.api.*;
import com.kuge.mall.order.component.AliPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/31
 */
@Configuration
public class AliPayConfig {
    @Resource
    private AliPayProperties aliPayProperties;

    @Bean
    public AlipayClient alipayClient() {
        AlipayConfig alipayConfig = new AlipayConfig();

        //设置网关地址
        alipayConfig.setServerUrl(aliPayProperties.getGatewayUrl());

        //设置应用Id
        alipayConfig.setAppId(aliPayProperties.getAppId());

        //设置应用私钥
        alipayConfig.setPrivateKey(aliPayProperties.getMerchantPrivateKey());

        //设置请求格式，固定值json
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);

        //设置字符集
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);

        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(aliPayProperties.getAlipayPublicKey());

        //设置签名类型
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);

        //构造client
        try {
            return new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
