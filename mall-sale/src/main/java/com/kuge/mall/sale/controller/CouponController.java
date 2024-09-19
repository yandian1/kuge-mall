package com.kuge.mall.sale.controller;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.sale.service.CouponService;
import com.kuge.mall.sale.vo.CouponPageItemVo;
import com.kuge.mall.sale.vo.CouponPageReqVo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/31
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    @PostMapping("/pageList")
    public R<PageData<CouponPageItemVo>> pageList(@RequestBody CouponPageReqVo orderPageReqVo, HttpServletRequest request) {
        return couponService.pageList(orderPageReqVo, request);
    }
}
