package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/21
 */
public interface CouponService {
    R<Void> add(CouponAddVo couponAddVo);

    R<Void> updateCoupon(CouponUpdateVo couponUpdateVo);

    R<Void> del(Long id);

    R<PageData<CouponPageItemVo>> pageList(CouponPageReqVo couponPageReqVo);

    R<CouponDetailVo> getCoupon(Long id);
}
