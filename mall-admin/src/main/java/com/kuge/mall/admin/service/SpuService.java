package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
public interface SpuService {
    R<Void> add(SpuAddVo spuAddVo);

    R<SpuDetailVo> getSpu(Long id);

    R<PageData<SpuPageResVo>> pageList(SpuPageReqVo spuPageVo);

    R<Void> updateSpu(SpuUpdateVo spuUpdateVo);

    R<Void> changeStatus(UpdateSpuStatusReqVo updateSpuStatusReqVo);

    R<Void> del(Long id);
}

