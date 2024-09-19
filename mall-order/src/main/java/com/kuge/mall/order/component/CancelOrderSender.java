package com.kuge.mall.order.component;

import com.kuge.mall.order.config.OrderConfig;
import com.kuge.mall.order.constant.QueueEnum;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * created by xbxie on 2024/7/4
 */
@Component
public class CancelOrderSender {

    @Resource
    private OrderConfig orderConfig;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Long orderId){
        // 给延迟队列发送消息
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //给消息设置延迟毫秒值
                message.getMessageProperties().setExpiration(String.valueOf(orderConfig.getDelayTimes()));
                return message;
            }
        });
    }
}

