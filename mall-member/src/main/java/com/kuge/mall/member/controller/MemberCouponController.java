package com.kuge.mall.member.controller;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.service.MemberCouponService;
import com.kuge.mall.member.vo.MemberCouponPageReqVo;
import com.kuge.mall.member.vo.MemberCouponPageItemVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@RestController
@RequestMapping("/member/coupon")
public class MemberCouponController {
    @Resource
    private MemberCouponService memberCouponService;

    @PostMapping("/pageList")
    public R<PageData<MemberCouponPageItemVo>> pageList(@RequestBody MemberCouponPageReqVo memberCouponPageReqVo, HttpServletRequest request) {
        return memberCouponService.pageList(memberCouponPageReqVo, request);
    }
}
