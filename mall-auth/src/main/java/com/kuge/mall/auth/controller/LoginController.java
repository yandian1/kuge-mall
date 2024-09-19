package com.kuge.mall.auth.controller;

import com.kuge.mall.auth.service.LoginService;
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
@RequestMapping("/login")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/admin")
    public R<AdminInfoVo> userLogin(@Validated @RequestBody LoginAdminReqVo loginAdminReqVo) {
        return loginService.userLogin(loginAdminReqVo);
    }

    @PostMapping("/member")
    public R<LoginMemberResVo> memberLogin(@Validated @RequestBody LoginMemberReqVo loginMemberReqVo) {
        return loginService.memberLogin(loginMemberReqVo);
    }
}
