package com.kuge.mall.auth.service;

import com.kuge.mall.auth.vo.*;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/23
 */
public interface LoginService {
    R<AdminInfoVo> userLogin(LoginAdminReqVo loginAdminReqVo);

    R<LoginMemberResVo> memberLogin(LoginMemberReqVo loginMemberReqVo);
}
