package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonSpuEntity;
import com.kuge.mall.common.mapper.CommonSpuMapper;
import com.kuge.mall.common.service.CommonSpuService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@Service("commonSpuService")
public class CommonSpuServiceImpl extends ServiceImpl<CommonSpuMapper, CommonSpuEntity> implements CommonSpuService {
}