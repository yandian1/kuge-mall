package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface CategoryService {
    R<PageData<CategoryPageResVo>> pageList(CategoryPageReqVo categoryPageReqVo);

    R<Long> add(CategoryAddVo categoryAddVo);

    R<List<CategoryTreeVo>> tree();

    R<Void> del(Long id);

    R<CategoryDetailVo> getCategory(Long id);

    R<Void> updateCategory(CategoryUpdateVo categoryUpdateVo);
}

