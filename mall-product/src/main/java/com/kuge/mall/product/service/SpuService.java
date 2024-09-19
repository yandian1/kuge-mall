package com.kuge.mall.product.service;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.vo.SpuDetailVo;
import com.kuge.mall.product.vo.SpuPageReqVo;
import com.kuge.mall.product.vo.SpuPageResVo;

import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
public interface SpuService {
    R<PageData<SpuPageResVo>> pageList(SpuPageReqVo spuPageVo, HttpServletRequest request);

    R<SpuDetailVo> getSpu(Long id, HttpServletRequest request);
}

