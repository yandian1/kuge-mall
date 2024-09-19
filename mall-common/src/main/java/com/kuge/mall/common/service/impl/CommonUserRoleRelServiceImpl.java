package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonUserRoleRelEntity;
import com.kuge.mall.common.mapper.CommonUserRoleRelMapper;
import com.kuge.mall.common.service.CommonUserRoleRelService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024/4/27
 */
@Service("commonUserRoleService")
public class CommonUserRoleRelServiceImpl extends ServiceImpl<CommonUserRoleRelMapper, CommonUserRoleRelEntity> implements CommonUserRoleRelService {
}
