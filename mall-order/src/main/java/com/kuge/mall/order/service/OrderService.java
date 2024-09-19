package com.kuge.mall.order.service;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.vo.*;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/21
 */
public interface OrderService {
    R<ConfirmOrderResVo> getConfirmInfo(ConfirmOrderReqVo confirmOrderReqVo, HttpServletRequest request);

    R<String> createOrder(CreateOrderReqVo createOrderReqVo, HttpServletRequest request);

    R<PageData<OrderPageItemVo>> pageList(OrderPageReqVo orderPageReqVo, HttpServletRequest request);

    R<OrderDetailVo> getOrder(Long id);

    R<Void> cancelOrder(Long orderId);

    R<Integer> unPayCount(HttpServletRequest request);
}
