package com.kuge.mall.order.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.vo.OrderItemDetailVo;

/**
 * created by xbxie on 2024/5/21
 */
public interface OrderItemService {
    R<OrderItemDetailVo> getOrderItemDetail(Long id);

    R<Void> receiveGoods(Long id);
}
