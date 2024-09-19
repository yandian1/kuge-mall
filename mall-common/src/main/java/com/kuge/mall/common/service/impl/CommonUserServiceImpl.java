package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.mapper.CommonUserMapper;
import com.kuge.mall.common.service.CommonUserService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024/4/27
 */
@Service("commonUserService")
public class CommonUserServiceImpl extends ServiceImpl<CommonUserMapper, CommonUserEntity> implements CommonUserService {
}
