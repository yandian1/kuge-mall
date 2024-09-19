package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.mapper.CommonMenuMapper;
import com.kuge.mall.common.service.CommonMenuService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024/4/25
 */
@Service("commonMenuService")
public class CommonMenuServiceImpl extends ServiceImpl<CommonMenuMapper, CommonMenuEntity> implements CommonMenuService {
}
