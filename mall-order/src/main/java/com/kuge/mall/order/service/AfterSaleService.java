package com.kuge.mall.order.service;


import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.vo.AfterSaleAddVo;
import com.kuge.mall.order.vo.AfterSalePageReqVo;
import com.kuge.mall.order.vo.AfterSalePageResVo;
import com.kuge.mall.order.vo.AfterSaleVo;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/21
 */
public interface AfterSaleService {
    R<Long> apply(AfterSaleAddVo afterSaleAddVo, HttpServletRequest request);

    R<AfterSaleVo> getAfterSale(Long id);

    R<PageData<AfterSalePageResVo>> pageList(AfterSalePageReqVo afterSalePageReqVo);

    R<Void> cancel(Long id);
}
