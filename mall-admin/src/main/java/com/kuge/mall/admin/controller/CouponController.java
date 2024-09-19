package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.CouponService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/4/24
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody CouponAddVo couponAddVo) {
        return couponService.add(couponAddVo);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody CouponUpdateVo couponUpdateVo) {
        return couponService.updateCoupon(couponUpdateVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return couponService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<CouponPageItemVo>> pageList(@Validated @RequestBody CouponPageReqVo couponPageReqVo) {
        return couponService.pageList(couponPageReqVo);
    }

    @PostMapping("/{id}")
    public R<CouponDetailVo> getCoupon(@PathVariable("id") Long id) {
        return couponService.getCoupon(id);
    }
}
