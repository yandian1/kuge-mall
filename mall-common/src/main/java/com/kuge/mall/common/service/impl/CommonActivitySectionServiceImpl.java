package com.kuge.mall.common.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.mapper.CommonActivitySectionMapper;
import com.kuge.mall.common.entity.CommonActivitySectionEntity;
import com.kuge.mall.common.service.CommonActivitySectionService;

/**
 * created by xbxie on 2024-08-08 04:34:38
 */
@Service("commonActivitySectionService")
public class CommonActivitySectionServiceImpl extends ServiceImpl<CommonActivitySectionMapper, CommonActivitySectionEntity> implements CommonActivitySectionService {
}