package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/21
 */
public interface AfterSaleService {
    R<PageData<AfterSalePageResVo>> pageList(AfterSalePageReqVo afterSalePageReqVo);

    R<AfterSaleVo> getAfterSale(Long id);

    R<Void> updateAfterSale(AfterSaleUpdateVo reqVo);

    void notify(HttpServletRequest request);
}
