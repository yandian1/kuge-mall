package com.kuge.mall.auth.controller;

import com.kuge.mall.auth.service.InfoService;
import com.kuge.mall.auth.vo.AdminInfoVo;
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
@RequestMapping("/info")
public class InfoController {
    @Resource
    private InfoService infoService;

    @PostMapping("/admin")
    public R<AdminInfoVo> adminInfo(HttpServletRequest request) {
        return infoService.adminInfo(request);
    }
}
