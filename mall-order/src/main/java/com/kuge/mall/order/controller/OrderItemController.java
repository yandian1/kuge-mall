package com.kuge.mall.order.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.OrderItemService;
import com.kuge.mall.order.vo.OrderItemDetailVo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/21
 */
@RestController
@RequestMapping("/order/item")
public class OrderItemController {
    @Resource
    private OrderItemService orderItemService;

    @PostMapping("/{id}")
    public R<OrderItemDetailVo> getOrderItemDetail(@PathVariable("id") Long id) {
        return orderItemService.getOrderItemDetail(id);
    }


    @PostMapping("/receiveGoods/{id}")
    public R<Void> receiveGoods(@PathVariable("id") Long id) {
        return orderItemService.receiveGoods(id);
    }
}
