package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.mapper.CommonMemberMapper;
import com.kuge.mall.common.service.CommonMemberService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024/4/27
 */
@Service("commonMemberService")
public class CommonMemberServiceImpl extends ServiceImpl<CommonMemberMapper, CommonMemberEntity> implements CommonMemberService {
}
