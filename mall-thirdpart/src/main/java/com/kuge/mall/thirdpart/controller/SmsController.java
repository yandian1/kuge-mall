package com.kuge.mall.thirdpart.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.thirdpart.service.SmsService;
import com.kuge.mall.thirdpart.vo.SmsCodeReqVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 发送短信控制器
 * created by xbxie on 2024/5/13
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Resource
    private SmsService smsService;

    @PostMapping("/sendCode")
    public R<Void> sendCode(@Validated @RequestBody SmsCodeReqVo smsCodeReqVo) {
        return smsService.sendCode(smsCodeReqVo);
    }
}
