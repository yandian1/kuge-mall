package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.admin.service.OrderService;
import com.kuge.mall.admin.vo.OrderDetailVo;
import com.kuge.mall.admin.vo.OrderPageReqVo;
import com.kuge.mall.admin.vo.OrderPageItemVo;
import com.kuge.mall.admin.vo.OrderSendGoodsReqVo;
import com.kuge.mall.common.constant.OrderStatusEnum;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.CommonOrderItemService;
import com.kuge.mall.common.service.CommonOrderService;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Resource
    private CommonShopService commonShopService;

    @Override
    public R<PageData<OrderPageItemVo>> pageList(OrderPageReqVo orderPageReqVo) {
        QueryWrapper<CommonOrderEntity> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("create_time");

        // 手机号
        String phone = orderPageReqVo.getPhone();
        if (StringUtils.hasLength(phone)) {
            wrapper.like("delivery_phone", phone);
        }

        // 订单号
        String orderNo = orderPageReqVo.getOrderNo();
        if (StringUtils.hasLength(orderNo)) {
            wrapper.eq("sn", orderNo);
        }

        // 订单状态
        String status = orderPageReqVo.getStatus();
        if (status != null) {
            wrapper.eq("status", status);
        }

        // 支付状态
        String payStatus = orderPageReqVo.getPayStatus();
        if (payStatus != null) {
            wrapper.eq("pay_status", payStatus);
        }


        String goodsName = orderPageReqVo.getGoodsName();
        List<OrderPageItemVo> orderPageItemVos = commonOrderService.list(wrapper).stream().map(commonOrderEntity -> {
            List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(
                new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId())
            );

            if (StringUtils.hasLength(goodsName) && commonOrderItemEntities.stream().noneMatch(commonOrderItemEntity -> commonOrderItemEntity.getSkuName().contains(goodsName))) {
                return null;
            }

            OrderPageItemVo orderPageItemVo = new OrderPageItemVo();

            orderPageItemVo.setId(commonOrderEntity.getId());
            orderPageItemVo.setSn(commonOrderEntity.getSn());
            orderPageItemVo.setPhone(commonOrderEntity.getReceiverPhone());
            orderPageItemVo.setStatus(commonOrderEntity.getStatus());
            orderPageItemVo.setStatusDesc(OrderUtils.getStatusDesc(commonOrderEntity.getStatus()));
            orderPageItemVo.setPayStatus(commonOrderEntity.getPayStatus());
            orderPageItemVo.setPayStatusDesc(PayUtils.getStatusDesc(commonOrderEntity.getPayStatus()));
            orderPageItemVo.setCreateTime(commonOrderEntity.getCreateTime());
            orderPageItemVo.setUpdateTime(commonOrderEntity.getUpdateTime());
            orderPageItemVo.setOrderItems(
                commonOrderItemEntities.stream().map(commonOrderItemEntity -> {
                    OrderPageItemVo.OrderItem orderItem = new OrderPageItemVo.OrderItem();
                    orderItem.setId(commonOrderItemEntity.getId());
                    orderItem.setStatus(commonOrderItemEntity.getStatus());
                    orderItem.setStatusDesc(OrderUtils.getStatusDesc(commonOrderItemEntity.getStatus()));
                    orderItem.setPayStatus(commonOrderItemEntity.getPayStatus());
                    orderItem.setPayStatusDesc(PayUtils.getStatusDesc(commonOrderItemEntity.getPayStatus()));
                    orderItem.setName(commonOrderItemEntity.getSkuName());
                    orderItem.setNum(commonOrderItemEntity.getSkuNum());
                    orderItem.setImg(commonOrderItemEntity.getSkuImg());
                    orderItem.setAttrs(GoodsUtils.getAttrValues(commonOrderItemEntity.getSkuAttrs()));
                    return orderItem;
                }).collect(Collectors.toList())
            );

            return orderPageItemVo;

        }).filter(Objects::nonNull).collect(Collectors.toList());

        PageData<OrderPageItemVo> pageData = PageData.getPageData(orderPageReqVo.getPageNum(), orderPageReqVo.getPageSize(), orderPageItemVos);

        return R.success(pageData);
    }

    @Override
    public R<Void> sendGoods(OrderSendGoodsReqVo orderSendGoodsReqVo) {
        CommonOrderItemEntity commonOrderItemEntity = commonOrderItemService.getById(orderSendGoodsReqVo.getId());
        if (commonOrderItemEntity == null) {
            throw new CustomException("子订单不存在");
        }

        commonOrderItemEntity.setDeliveryCompany(orderSendGoodsReqVo.getDeliveryCompany());
        commonOrderItemEntity.setDeliverySn(orderSendGoodsReqVo.getDeliverySn());
        commonOrderItemEntity.setStatus(OrderStatusEnum.UN_RECEIVE.getCode());

        if (!commonOrderItemService.updateById(commonOrderItemEntity)) {
            throw new CustomException("发货失败");
        }


        CommonOrderEntity commonOrderEntity = commonOrderService.getById(commonOrderItemEntity.getOrderId());
        commonOrderEntity.setStatus(OrderStatusEnum.UN_RECEIVE.getCode());
        if (!commonOrderService.updateById(commonOrderEntity)) {
            throw new CustomException("发货失败");
        }


        return R.success("发货成功");
    }

    @Override
    public R<OrderDetailVo> getOrder(Long id) {
        // 订单不存在
        CommonOrderEntity commonOrderEntity = commonOrderService.getById(id);
        if (commonOrderEntity == null) {
            throw new CustomException("订单不存在");
        }

        BigDecimal price = BigDecimal.ZERO;
        List<OrderDetailVo.OrderGoods> orderGoodsList = new ArrayList<>();
        List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));

        for (CommonOrderItemEntity commonOrderItemEntity : commonOrderItemEntities) {

            OrderDetailVo.OrderGoods orderGoods = new OrderDetailVo.OrderGoods();

            orderGoods.setId(commonOrderItemEntity.getId());
            orderGoods.setNum(commonOrderItemEntity.getSkuNum());
            orderGoods.setAttrs(GoodsUtils.getAttrValues(commonOrderItemEntity.getSkuAttrs()));
            orderGoods.setName(commonOrderItemEntity.getSkuName());
            orderGoods.setImg(commonOrderItemEntity.getSkuImg());
            orderGoods.setStatus(commonOrderItemEntity.getStatus());
            orderGoods.setStatusDesc(OrderUtils.getStatusDesc(commonOrderItemEntity.getStatus()));
            orderGoods.setPayStatus(commonOrderItemEntity.getPayStatus());
            orderGoods.setPayStatusDesc(PayUtils.getStatusDesc(commonOrderItemEntity.getPayStatus()));
            orderGoods.setDeliveryCompany(commonOrderItemEntity.getDeliveryCompany());
            orderGoods.setDeliverySn(commonOrderItemEntity.getDeliverySn());

            BigDecimal actualAmount = OrderUtils.calcActualAmount(commonOrderItemEntity);
            orderGoods.setPrice(actualAmount);

            orderGoodsList.add(orderGoods);

            price = price.add(actualAmount);
        }

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setId(id);
        orderDetailVo.setSn(commonOrderEntity.getSn());
        orderDetailVo.setShopName(commonShopService.getById(commonOrderEntity.getShopId()).getName());
        orderDetailVo.setPrice(price);
        orderDetailVo.setStatus(commonOrderEntity.getStatus());
        orderDetailVo.setStatusDesc(OrderUtils.getStatusDesc(commonOrderEntity.getStatus()));
        orderDetailVo.setPayStatus(commonOrderEntity.getPayStatus());
        orderDetailVo.setPayStatusDesc(PayUtils.getStatusDesc(commonOrderEntity.getPayStatus()));
        orderDetailVo.setOrderGoodsList(orderGoodsList);
        orderDetailVo.setReceiverName(commonOrderEntity.getReceiverName());
        orderDetailVo.setReceiverPhone(commonOrderEntity.getReceiverPhone());
        orderDetailVo.setReceiverAddress(commonOrderEntity.getReceiverAddress());

        return R.success(orderDetailVo);
    }
}