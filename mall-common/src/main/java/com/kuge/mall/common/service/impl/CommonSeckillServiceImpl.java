package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonSeckillEntity;
import com.kuge.mall.common.mapper.CommonSeckillMapper;
import com.kuge.mall.common.service.CommonSeckillService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-23 18:28:12
 */
@Service("commonSeckillService")
public class CommonSeckillServiceImpl extends ServiceImpl<CommonSeckillMapper, CommonSeckillEntity> implements CommonSeckillService {
}