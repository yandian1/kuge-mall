package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonFreightEntity;
import com.kuge.mall.common.mapper.CommonFreightMapper;
import com.kuge.mall.common.service.CommonFreightService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Service("commonFreightService")
public class CommonFreightServiceImpl extends ServiceImpl<CommonFreightMapper, CommonFreightEntity> implements CommonFreightService {
}