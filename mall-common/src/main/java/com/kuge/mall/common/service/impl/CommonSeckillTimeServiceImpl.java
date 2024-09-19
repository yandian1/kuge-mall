package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonSeckillTimeEntity;
import com.kuge.mall.common.mapper.CommonSeckillTimeMapper;
import com.kuge.mall.common.service.CommonSeckillTimeService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Service("commonSeckillTimeService")
public class CommonSeckillTimeServiceImpl extends ServiceImpl<CommonSeckillTimeMapper, CommonSeckillTimeEntity> implements CommonSeckillTimeService {
}