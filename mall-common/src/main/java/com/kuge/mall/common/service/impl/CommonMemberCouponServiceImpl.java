package com.kuge.mall.common.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.mapper.CommonMemberCouponMapper;
import com.kuge.mall.common.entity.CommonMemberCouponEntity;
import com.kuge.mall.common.service.CommonMemberCouponService;

/**
 * created by xbxie on 2024-07-13 04:17:39
 */
@Service("commonMemberCouponService")
public class CommonMemberCouponServiceImpl extends ServiceImpl<CommonMemberCouponMapper, CommonMemberCouponEntity> implements CommonMemberCouponService {
}