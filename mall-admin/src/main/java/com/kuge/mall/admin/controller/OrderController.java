package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.OrderService;
import com.kuge.mall.admin.vo.OrderDetailVo;
import com.kuge.mall.admin.vo.OrderPageReqVo;
import com.kuge.mall.admin.vo.OrderPageItemVo;
import com.kuge.mall.admin.vo.OrderSendGoodsReqVo;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/pageList")
    public R<PageData<OrderPageItemVo>> pageList(@RequestBody OrderPageReqVo orderPageReqVo) {
        return orderService.pageList(orderPageReqVo);
    }

    @PostMapping("/sendGoods")
    public R<Void> sendGoods(@RequestBody OrderSendGoodsReqVo orderSendGoodsReqVo) {
        return orderService.sendGoods(orderSendGoodsReqVo);
    }

    @PostMapping("/{id}")
    public R<OrderDetailVo> getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }
}
