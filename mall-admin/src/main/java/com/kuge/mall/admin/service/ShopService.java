package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface ShopService {
    R<List<ShopVo>> list();

    R<Void> add(ShopAddVo shopAddVo);

    R<Void> updateShop(ShopUpdateVo shopUpdateVo);

    R<ShopDetailVo> getShop(Long id);

    R<Void> del(Long id);

    R<PageData<ShopPageResVo>> pageList(ShopPageReqVo brandPageReqVo);
}

