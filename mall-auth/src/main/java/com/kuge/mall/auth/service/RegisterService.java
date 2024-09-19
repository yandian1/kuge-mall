package com.kuge.mall.auth.service;

import com.kuge.mall.auth.vo.RegisterMemberReqVo;
import com.kuge.mall.auth.vo.RegisterMemberResVo;
import com.kuge.mall.auth.vo.RegisterUserReqVo;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/23
 */
public interface RegisterService {
    R<RegisterMemberResVo> memberRegister(RegisterMemberReqVo registerMemberReqVo);

    R<Void> userRegister(RegisterUserReqVo registerUserReqVo);
}
