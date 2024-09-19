package com.kuge.mall.auth.controller;

import com.kuge.mall.auth.service.RegisterService;
import com.kuge.mall.auth.vo.*;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/23
 */
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Resource
    private RegisterService registerService;

    @PostMapping("/user")
    public R<Void> userRegister(@Validated @RequestBody RegisterUserReqVo registerUserReqVo) {
        return registerService.userRegister(registerUserReqVo);
    }

    @PostMapping("/member")
    public R<RegisterMemberResVo> memberRegister(@Validated @RequestBody RegisterMemberReqVo registerMemberReqVo) {
        return registerService.memberRegister(registerMemberReqVo);
    }
}
