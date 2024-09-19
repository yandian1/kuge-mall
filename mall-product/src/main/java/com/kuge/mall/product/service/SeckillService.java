package com.kuge.mall.product.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.vo.SeckillDetailVo;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface SeckillService {
    R<SeckillDetailVo> getSeckill(Long id);
}

