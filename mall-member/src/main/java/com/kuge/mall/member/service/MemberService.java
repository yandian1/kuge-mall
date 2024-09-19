package com.kuge.mall.member.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.vo.AccountReqVo;
import com.kuge.mall.member.vo.NameReqVo;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
public interface MemberService {
    R<Void> setName(NameReqVo nameReqVo, HttpServletRequest request);

    R<Void> setAccount(AccountReqVo accountReqVo, HttpServletRequest request);
}
