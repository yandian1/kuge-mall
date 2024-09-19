package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonCategoryEntity;
import com.kuge.mall.common.mapper.CommonCategoryMapper;
import com.kuge.mall.common.service.CommonCategoryService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("commonCategoryService")
public class CommonCategoryServiceImpl extends ServiceImpl<CommonCategoryMapper, CommonCategoryEntity> implements CommonCategoryService {
}