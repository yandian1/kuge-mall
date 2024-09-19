package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface BrandService {
    R<PageData<BrandPageResVo>> pageList(BrandPageReqVo brandPageReqVo);

    R<Void> add(BrandAddVo brandAddVo);

    R<Void> updateBrand(BrandUpdateVo brandUpdateVo);

    R<BrandDetailVo> getBrand(Long id);

    R<Void> del(Long id);

    R<List<BrandDetailVo>> getList();
}

