package com.kuge.mall.common.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.mapper.CommonActivityMapper;
import com.kuge.mall.common.entity.CommonActivityEntity;
import com.kuge.mall.common.service.CommonActivityService;

/**
 * created by xbxie on 2024-08-08 04:34:38
 */
@Service("commonActivityService")
public class CommonActivityServiceImpl extends ServiceImpl<CommonActivityMapper, CommonActivityEntity> implements CommonActivityService {
}