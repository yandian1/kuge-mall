package com.kuge.mall.product.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.vo.ActivitylDetailVo;

/**
 * created by xbxie on 2024/5/21
 */
public interface ActivityService {
    R<ActivitylDetailVo> getActivity(Long id);
}
