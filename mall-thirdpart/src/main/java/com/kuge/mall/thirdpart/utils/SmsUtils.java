package com.kuge.mall.thirdpart.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.kuge.mall.thirdpart.config.SmsConfig;
import org.springframework.stereotype.Component;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teautil.models.RuntimeOptions;
import javax.annotation.Resource;
import java.util.Random;

/**
 * created by xbxie on 2024/5/17
 */
@Component
public class SmsUtils {

    @Resource
    private Client client;

    @Resource
    private SmsConfig smsConfig;

    public String genVerifyCode()  {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int j = 0; j < 6; j++) {
            String num = String.valueOf(random.nextInt(10));
            builder.append(num);
        }
        return builder.toString();
    }

    public void sendVerifyCode(String phone, String code)  {
        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(smsConfig.getSign())
                .setTemplateCode(smsConfig.getTemplate())
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+code+"\"}");
            RuntimeOptions runtime = new RuntimeOptions();
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            System.out.println("message：" + sendSmsResponse.getBody().getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
