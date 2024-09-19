package com.kuge.mall.common.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.mapper.CommonCouponMapper;
import com.kuge.mall.common.entity.CommonCouponEntity;
import com.kuge.mall.common.service.CommonCouponService;

/**
 * created by xbxie on 2024-07-13 01:23:16
 */
@Service("commonCouponService")
public class CommonCouponServiceImpl extends ServiceImpl<CommonCouponMapper, CommonCouponEntity> implements CommonCouponService {
}