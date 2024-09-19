package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonRoleEntity;
import com.kuge.mall.common.mapper.CommonRoleMapper;
import com.kuge.mall.common.service.CommonRoleService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024/4/25
 */
@Service("commonRoleService")
public class CommonRoleServiceImpl extends ServiceImpl<CommonRoleMapper, CommonRoleEntity> implements CommonRoleService {
}
