package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.MemberService;
import com.kuge.mall.admin.vo.MemberPageItemVo;
import com.kuge.mall.admin.vo.MemberPageReqVo;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/pageList")
    public R<PageData<MemberPageItemVo>> pageList(@RequestBody MemberPageReqVo memberPageReqVo) {
        return memberService.pageList(memberPageReqVo);
    }
}
