package com.kuge.mall.order.controller;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.OrderService;
import com.kuge.mall.order.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/21
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/confirmInfo")
    public R<ConfirmOrderResVo> getConfirmInfo(@Validated @RequestBody ConfirmOrderReqVo confirmOrderReqVo, HttpServletRequest request) {
        return orderService.getConfirmInfo(confirmOrderReqVo, request);
    }

    @PostMapping("/create")
    public R<String> createOrder(@Validated @RequestBody CreateOrderReqVo createOrderReqVo, HttpServletRequest request) {
        return orderService.createOrder(createOrderReqVo, request);
    }

    @PostMapping("/pageList")
    public R<PageData<OrderPageItemVo>> pageList(@RequestBody OrderPageReqVo orderPageReqVo, HttpServletRequest request) {
        return orderService.pageList(orderPageReqVo, request);
    }

    @PostMapping("/{id}")
    public R<OrderDetailVo> getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }


    @PostMapping("/count/unPay")
    public R<Integer> unPayCount(HttpServletRequest request) {
        return orderService.unPayCount(request);
    }

    @PostMapping("/cancel/{id}")
    public R<Void> cancelOrder(@PathVariable("id") Long id) {
        return orderService.cancelOrder(id);
    }
}
