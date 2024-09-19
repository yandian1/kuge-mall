package com.kuge.mall.order.component;

import com.kuge.mall.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * created by xbxie on 2024/7/4
 */
@Component
@RabbitListener(queues = "mall.order.cancel")
public class CancelOrderReceiver {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void handle(Long orderId){
        orderService.cancelOrder(orderId);
    }
}
