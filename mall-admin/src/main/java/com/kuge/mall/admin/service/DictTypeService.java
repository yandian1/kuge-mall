package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
public interface DictTypeService  {
    R<Void> add(DictTypeAddVo dictTypeAddVo);

    R<Void> del(Long id);

    R<Void> updateDictType(DictTypeUpdateVo dictTypeUpdateVo);

    R<PageData<DictTypePageResVo>> pageList(DictTypePageReqVo dictTypePageReqVo);

    R<DictTypeDetailVo> getDictType(Long id);
}

