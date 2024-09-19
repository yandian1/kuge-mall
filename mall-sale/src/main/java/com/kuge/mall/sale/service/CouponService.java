package com.kuge.mall.sale.service;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.sale.vo.CouponPageItemVo;
import com.kuge.mall.sale.vo.CouponPageReqVo;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/31
 */
public interface CouponService {
    R<PageData<CouponPageItemVo>> pageList(CouponPageReqVo orderPageReqVo, HttpServletRequest request);
}
