package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/21
 */
public interface SeckillService {
    R<Void> add(SeckillAddVo seckillAddVo);

    R<Void> updateSeckill(SeckillUpdateVo seckillUpdateVo);

    R<Void> del(Long id);

    R<PageData<SeckillPageItemVo>> pageList(SeckillPageReqVo seckillPageReqVo);

    R<SeckillDetailVo> getSeckill(Long id);
}
