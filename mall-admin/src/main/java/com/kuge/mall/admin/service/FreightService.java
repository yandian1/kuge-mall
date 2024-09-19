package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface FreightService {
    R<Void> add(FreightAddVo freightAddVo);

    R<Void> updateFreight(FreightUpdateVo freightUpdateVo);

    R<Void> del(Long id);

    R<PageData<FreightPageItemVo>> pageList(FreightPageReqVo freightPageReqVo);

    R<FreightDetailVo> getFreight(Long id);

    R<List<FreightItemVo>> list();
}

