package com.kuge.mall.member.service;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.vo.MemberCouponPageItemVo;
import com.kuge.mall.member.vo.MemberCouponPageReqVo;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
public interface MemberCouponService {
    R<PageData<MemberCouponPageItemVo>> pageList(MemberCouponPageReqVo memberCouponPageReqVo, HttpServletRequest request);
}
