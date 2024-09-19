package com.kuge.mall.member.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.service.MemberService;
import com.kuge.mall.member.vo.AccountReqVo;
import com.kuge.mall.member.vo.NameReqVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/setName")
    public R<Void> setName(@Validated @RequestBody NameReqVo nameReqVo, HttpServletRequest request) {
        return memberService.setName(nameReqVo, request);
    }

    @PostMapping("/setAccount")
    public R<Void> setAccount(@Validated @RequestBody AccountReqVo accountReqVo, HttpServletRequest request) {
        return memberService.setAccount(accountReqVo, request);
    }
}
