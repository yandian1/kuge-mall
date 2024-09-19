package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonBrandEntity;
import com.kuge.mall.common.mapper.CommonBrandMapper;
import com.kuge.mall.common.service.CommonBrandService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("commonBrandService")
public class CommonBrandServiceImpl extends ServiceImpl<CommonBrandMapper, CommonBrandEntity> implements CommonBrandService {
}