package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import java.util.List;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
public interface DictValueService {
    R<Void> add(DictValueAddVo dictValueAddVo);

    R<Void> del(Long id);

    R<Void> updateDict(DictValueUpdateVo dictValueUpdateVo);

    R<PageData<DictValuePageResVo>> pageList(DictValuePageReqVo dictValuePageReqVo);

    R<DictValueDetailVo> getDictValue(Long id);

    R<List<DictValueListItemVo>> list(String code);
}

