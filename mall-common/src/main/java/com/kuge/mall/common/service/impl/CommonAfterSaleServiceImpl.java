package com.kuge.mall.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuge.mall.common.entity.CommonAfterSaleEntity;
import com.kuge.mall.common.mapper.CommonAfterSaleMapper;
import com.kuge.mall.common.service.CommonAfterSaleService;
import org.springframework.stereotype.Service;

/**
 * created by xbxie on 2024-06-07 16:15:20
 */
@Service("commonAfterSaleService")
public class CommonAfterSaleServiceImpl extends ServiceImpl<CommonAfterSaleMapper, CommonAfterSaleEntity> implements CommonAfterSaleService {
}