package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.MemberPageItemVo;
import com.kuge.mall.admin.vo.MemberPageReqVo;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/23
 */
public interface MemberService {
    R<PageData<MemberPageItemVo>> pageList(MemberPageReqVo memberPageReqVo);
}
