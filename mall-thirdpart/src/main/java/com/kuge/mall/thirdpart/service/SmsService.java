package com.kuge.mall.thirdpart.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.thirdpart.vo.SmsCodeReqVo;

/**
 * created by xbxie on 2024/5/23
 */
public interface SmsService {
    R<Void> sendCode(SmsCodeReqVo smsCodeReqVo);
}
