package com.kuge.mall.order.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.vo.*;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/31
 */
public interface PayService {
    R<String> wxPayStatus(PayStatusReqVo payStatusReqVo);

    R<PreInfoResVo> getPreInfo(PreInfoReqVo preInfoReqVo);

    void notify(HttpServletRequest request);

    R<String> getCodeUrl(CodeUrlReqVo codeUrlReqVo);

    R<PayResultResVo> wxPayResult(PayResultReqVo payResultReqVo);
}
