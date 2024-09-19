package com.kuge.mall.common.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.mapper.CommonCouponSpuMapper;
import com.kuge.mall.common.entity.CommonCouponSpuEntity;
import com.kuge.mall.common.service.CommonCouponSpuService;

/**
 * created by xbxie on 2024-07-13 04:17:39
 */
@Service("commonCouponSpuService")
public class CommonCouponSpuServiceImpl extends ServiceImpl<CommonCouponSpuMapper, CommonCouponSpuEntity> implements CommonCouponSpuService {
}