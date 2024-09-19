package com.kuge.mall.auth.service;

import com.kuge.mall.auth.vo.AdminInfoVo;
import com.kuge.mall.common.utils.R;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
public interface InfoService {
    R<AdminInfoVo> adminInfo(HttpServletRequest request);
}
