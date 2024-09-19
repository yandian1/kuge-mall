package com.kuge.mall.auth.controller;

import com.kuge.mall.auth.service.LogoutService;
import com.kuge.mall.common.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {
    @Resource
    private LogoutService logoutService;

    @PostMapping("/admin")
    public R<Void> adminLogout(HttpServletRequest request) {
        return logoutService.userLogout(request);
    }

    @PostMapping("/member")
    public R<Void> memberLogout(HttpServletRequest request) {
        return logoutService.memberLogout(request);
    }
}
