package com.kuge.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.constant.OrderStatusEnum;
import com.kuge.mall.common.entity.CommonOrderEntity;
import com.kuge.mall.common.entity.CommonOrderItemEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.service.CommonOrderItemService;
import com.kuge.mall.common.service.CommonOrderService;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.GoodsUtils;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.OrderItemService;
import com.kuge.mall.order.vo.OrderGoods;
import com.kuge.mall.order.vo.OrderItemDetailVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * created by xbxie on 2024/5/21
 */
@Service("orderItemService")
public class OrderItemServiceImpl implements OrderItemService {

    @Resource
    private CommonShopService commonShopService;

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Override
    public R<OrderItemDetailVo> getOrderItemDetail(Long id) {
        CommonOrderItemEntity commonOrderItemEntity = commonOrderItemService.getById(id);
        if (commonOrderItemEntity == null) {
            throw new CustomException("订单不存在");
        }

        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setImg(commonOrderItemEntity.getSkuImg());
        orderGoods.setName(commonOrderItemEntity.getSkuName());
        orderGoods.setAttrs(GoodsUtils.getAttrValues(commonOrderItemEntity.getSkuAttrs()));
        orderGoods.setPrice(commonOrderItemEntity.getSkuPrice());
        orderGoods.setNum(commonOrderItemEntity.getSkuNum());

        CommonShopEntity commonShopEntity = commonShopService.getById(commonOrderItemEntity.getShopId());
        OrderItemDetailVo orderItemDetailVo = new OrderItemDetailVo();
        orderItemDetailVo.setShopName(commonShopEntity.getName());
        orderItemDetailVo.setOrderGoods(orderGoods);


        return R.success(orderItemDetailVo);
    }

    @Override
    public R<Void> receiveGoods(Long id) {
        CommonOrderItemEntity commonOrderItemEntity = commonOrderItemService.getById(id);
        if (commonOrderItemEntity == null) {
            throw new CustomException("订单不存在");
        }

        // 设置为已收货
        commonOrderItemEntity.setStatus(OrderStatusEnum.RECEIVED.getCode());
        if (!commonOrderItemService.updateById(commonOrderItemEntity)) {
            throw new CustomException("更新订单状态失败");
        }



        // 所有子订单都已完成
        if (
            commonOrderItemService.list(
                new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderItemEntity.getOrderId())
            ).stream().allMatch(item -> Objects.equals(item.getStatus(), OrderStatusEnum.RECEIVED.getCode()))
        ) {

            CommonOrderEntity commonOrderEntity = commonOrderService.getById(commonOrderItemEntity.getOrderId());
            commonOrderEntity.setStatus(OrderStatusEnum.RECEIVED.getCode());
            if (!commonOrderService.updateById(commonOrderEntity)) {
                throw new CustomException("更新订单状态失败");
            }
        }

        return R.success();
    }
}
