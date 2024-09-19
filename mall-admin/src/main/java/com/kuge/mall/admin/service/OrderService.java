package com.kuge.mall.admin.service;


import com.kuge.mall.admin.vo.OrderDetailVo;
import com.kuge.mall.admin.vo.OrderPageReqVo;
import com.kuge.mall.admin.vo.OrderPageItemVo;
import com.kuge.mall.admin.vo.OrderSendGoodsReqVo;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface OrderService {
    R<PageData<OrderPageItemVo>> pageList(OrderPageReqVo orderPageReqVo);

    R<Void> sendGoods(OrderSendGoodsReqVo orderSendGoodsReqVo);

    R<OrderDetailVo> getOrder(Long id);
}

