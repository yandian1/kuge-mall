package com.kuge.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.kuge.mall.common.constant.OrderStatusEnum;
import com.kuge.mall.common.constant.PayStatusEnum;
import com.kuge.mall.common.entity.CommonOrderEntity;
import com.kuge.mall.common.entity.CommonOrderItemEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.service.CommonOrderItemService;
import com.kuge.mall.common.service.CommonOrderService;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.OrderUtils;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.PayService;
import com.kuge.mall.order.utils.WxPayUtils;
import com.kuge.mall.order.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by xbxie on 2024/5/31
 */
@Service("payService")
public class PayServiceImpl implements PayService {

    @Resource
    private WxPayUtils wxPayUtils;

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Resource
    private CommonShopService commonShopService;

    @Override
    public R<String> wxPayStatus(PayStatusReqVo payStatusReqVo) {
        String batchSn = payStatusReqVo.getBatchSn();
        String sn = payStatusReqVo.getSn();

        if (!StringUtils.hasLength(batchSn) && !StringUtils.hasLength(sn)) {
            throw new CustomException("请输入订单号");
        }

        PayStatusEnum status = wxPayUtils.queryNativeOrder(StringUtils.hasLength(batchSn) ? batchSn : sn);

        if (status == null) {
            throw new CustomException("未查询到支付结果，请您联系客服进行确认~");
        }

        if (status == PayStatusEnum.PAID) {
            // updateOrderStatus(payStatusReqVo);
        }

        return R.successData(status.getCode());
    }

    @Override
    public R<PreInfoResVo> getPreInfo(PreInfoReqVo preInfoReqVo) {
        String batchSn = preInfoReqVo.getBatchSn();
        String sn = preInfoReqVo.getSn();

        if (!StringUtils.hasLength(batchSn) && !StringUtils.hasLength(sn)) {
            throw new CustomException("请输入订单号");
        }

        List<CommonOrderEntity> commonOrderEntities;

        // 合并支付
        if (StringUtils.hasLength(batchSn)) {
            commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", batchSn));
        } else {
            // 拆单支付
            commonOrderEntities = Stream.of(commonOrderService.getOne(new QueryWrapper<CommonOrderEntity>().eq("sn", sn), false)).collect(Collectors.toList());
        }

        List<CommonOrderItemEntity> commonOrderItemEntities = new ArrayList<>();

        for (CommonOrderEntity commonOrderEntity : commonOrderEntities) {
            String payStatus = commonOrderEntity.getPayStatus();
            if (payStatus == null || payStatus.equals(PayStatusEnum.UN_PAY.getCode())) {
                commonOrderItemEntities.addAll(
                    commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()))
                );
            } else {
                throw new CustomException("订单状态异常，请先联系客服");
            }
        }

        PreInfoResVo preInfoResVo = new PreInfoResVo();
        preInfoResVo.setPrice(OrderUtils.calcTotalActualAmount(commonOrderItemEntities));
        return R.successData(preInfoResVo);
    }

    @Override
    public R<String> getCodeUrl(CodeUrlReqVo codeUrlReqVo) {
        String batchSn = codeUrlReqVo.getBatchSn();
        String sn = codeUrlReqVo.getSn();

        if (!StringUtils.hasLength(batchSn) && !StringUtils.hasLength(sn)) {
            throw new CustomException("请输入订单号");
        }

        // 合并支付
        if (StringUtils.hasLength(batchSn)) {
            List<CommonOrderEntity> commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", batchSn));
            List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().in("order_id", commonOrderEntities.stream().map(CommonOrderEntity::getId).collect(Collectors.toList())));

            for (CommonOrderEntity commonOrderEntity : commonOrderEntities) {
                String payStatus = commonOrderEntity.getPayStatus();
                if (payStatus == null || payStatus.equals(PayStatusEnum.UN_PAY.getCode())) {
                    commonOrderEntity.setPayType(codeUrlReqVo.getPayType());
                    commonOrderEntity.setStatus(OrderStatusEnum.UN_PAY.getCode());
                    commonOrderEntity.setPayStatus(PayStatusEnum.UN_PAY.getCode());
                } else {
                    throw new CustomException("订单状态异常，请先联系客服");
                }
            }

            for (CommonOrderItemEntity commonOrderItemEntity : commonOrderItemEntities) {
                String payStatus = commonOrderItemEntity.getPayStatus();
                if (payStatus == null || payStatus.equals(PayStatusEnum.UN_PAY.getCode())) {
                    commonOrderItemEntity.setStatus(OrderStatusEnum.UN_PAY.getCode());
                    commonOrderItemEntity.setPayStatus(PayStatusEnum.UN_PAY.getCode());
                } else {
                    throw new CustomException("订单状态异常，请先联系客服");
                }
            }

            if (!commonOrderService.updateBatchById(commonOrderEntities)) {
                throw new CustomException("下单失败");
            }

            if (!commonOrderItemService.updateBatchById(commonOrderItemEntities)) {
                throw new CustomException("下单失败");
            }

            String name = commonShopService.listByIds(commonOrderEntities.stream().map(CommonOrderEntity::getShopId).collect(Collectors.toList())).stream().map(CommonShopEntity::getName).collect(Collectors.joining(","));
            int total = OrderUtils.calcTotalActualAmount(commonOrderItemEntities).multiply(BigDecimal.valueOf(100)).intValue();
            String codeUrl = wxPayUtils.createNativeOrder(total, batchSn, name, "batch");

            return R.successData(codeUrl);
        }

        // 拆单支付
        CommonOrderEntity commonOrderEntity = commonOrderService.getOne(new QueryWrapper<CommonOrderEntity>().eq("sn", sn), false);
        List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().in("order_id", commonOrderEntity.getId()));

        String orderPayStatus = commonOrderEntity.getPayStatus();
        if (orderPayStatus == null || orderPayStatus.equals(PayStatusEnum.UN_PAY.getCode())) {
            commonOrderEntity.setPayType(codeUrlReqVo.getPayType());
            commonOrderEntity.setStatus(OrderStatusEnum.UN_PAY.getCode());
            commonOrderEntity.setPayStatus(PayStatusEnum.UN_PAY.getCode());
        } else {
            throw new CustomException("订单状态异常，请先联系客服");
        }

        for (CommonOrderItemEntity commonOrderItemEntity : commonOrderItemEntities) {
            String orderItemPayStatus = commonOrderItemEntity.getPayStatus();
            if (orderItemPayStatus == null || orderItemPayStatus.equals(PayStatusEnum.UN_PAY.getCode())) {
                commonOrderItemEntity.setStatus(OrderStatusEnum.UN_PAY.getCode());
                commonOrderItemEntity.setPayStatus(PayStatusEnum.UN_PAY.getCode());
            } else {
                throw new CustomException("订单状态异常，请先联系客服");
            }
        }

        if (!commonOrderService.updateById(commonOrderEntity)) {
            throw new CustomException("支付失败");
        }

        if (!commonOrderItemService.updateBatchById(commonOrderItemEntities)) {
            throw new CustomException("下单失败");
        }

        int total = OrderUtils.calcTotalActualAmount(commonOrderItemEntities).multiply(BigDecimal.valueOf(100)).intValue();
        CommonShopEntity commonShopEntity = commonShopService.getById(commonOrderEntity.getShopId());
        String codeUrl = wxPayUtils.createNativeOrder(total, sn, commonShopEntity.getName(), "single");

        return R.successData(codeUrl);
    }

    @Override
    public R<PayResultResVo> wxPayResult(PayResultReqVo payResultReqVo) {
        String batchSn = payResultReqVo.getBatchSn();
        String sn = payResultReqVo.getSn();

        if (!StringUtils.hasLength(batchSn) && !StringUtils.hasLength(sn)) {
            throw new CustomException("请输入订单号");
        }

        List<CommonOrderItemEntity> orderItemEntities = getOrderItemEntities(payResultReqVo.getBatchSn(), payResultReqVo.getSn());

        // if (
        //     orderItemEntities.stream().anyMatch(orderItemEntity -> !Objects.equals(orderItemEntity.getStatus(), PayStatusEnum.PAID.getCode()))
        // ) {
        //     throw new CustomException("订单未成功支付");
        // }

        BigDecimal totalActualAmount = OrderUtils.calcTotalActualAmount(orderItemEntities);
        PayResultResVo payResultResVo = new PayResultResVo();
        payResultResVo.setAmount(totalActualAmount);
        payResultResVo.setPayStatus(PayStatusEnum.PAID.getCode());

        return R.successData(payResultResVo);
    }

    @Override
    public void notify(HttpServletRequest request) {
        System.out.println("parsePayNotify");
        Transaction transaction = wxPayUtils.parsePayNotify(request);
        String attach = transaction.getAttach();

        // 支付成功
        if (transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
            String outTradeNo = transaction.getOutTradeNo();
            if (Objects.equals(attach, "batch")) {
                // 批量支付
                // 父订单
                List<CommonOrderEntity> commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", outTradeNo));
                commonOrderEntities.forEach(commonOrderEntity -> {
                    commonOrderEntity.setBatchPay(1);
                    commonOrderEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                    commonOrderEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                    // 子订单
                    List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));
                    commonOrderItemEntities.forEach(commonOrderItemEntity -> {
                        commonOrderItemEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                        commonOrderItemEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                    });
                    commonOrderItemService.updateBatchById(commonOrderItemEntities);
                });
                commonOrderService.updateBatchById(commonOrderEntities);
            } else if (Objects.equals(attach, "single")) {
                // 单独支付
                // 父订单
                CommonOrderEntity commonOrderEntity = commonOrderService.getOne(new QueryWrapper<CommonOrderEntity>().eq("sn", outTradeNo), false);
                commonOrderEntity.setBatchPay(0);
                commonOrderEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                commonOrderEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                commonOrderService.updateById(commonOrderEntity);

                // 子订单
                List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));
                commonOrderItemEntities.forEach(commonOrderItemEntity -> {
                    commonOrderItemEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                    commonOrderItemEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                });
                commonOrderItemService.updateBatchById(commonOrderItemEntities);
            }
        }
    }

    public void updateOrderStatus(PayStatusReqVo payStatusReqVo) {
        if (StringUtils.hasLength(payStatusReqVo.getBatchSn())) {
            // 批量支付
            // 父订单
            List<CommonOrderEntity> commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", payStatusReqVo.getBatchSn()));
            if (
                commonOrderEntities.stream().anyMatch(commonOrderEntity -> commonOrderEntity.getPayStatus() != null)
            ) {
                return;
            }

            commonOrderEntities.forEach(commonOrderEntity -> {
                commonOrderEntity.setBatchPay(1);
                commonOrderEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                commonOrderEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                // 子订单
                List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));
                commonOrderItemEntities.forEach(commonOrderItemEntity -> {
                    commonOrderItemEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                    commonOrderItemEntity.setPayStatus(PayStatusEnum.PAID.getCode());
                });
                commonOrderItemService.updateBatchById(commonOrderItemEntities);
            });
            commonOrderService.updateBatchById(commonOrderEntities);
        } else if (StringUtils.hasLength(payStatusReqVo.getSn())) {
            // 单独支付
            // 父订单
            CommonOrderEntity commonOrderEntity = commonOrderService.getOne(new QueryWrapper<CommonOrderEntity>().eq("sn", payStatusReqVo.getSn()), false);
            if (commonOrderEntity.getPayStatus() != null) {
                return;
            }
            commonOrderEntity.setBatchPay(0);
            commonOrderEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
            commonOrderEntity.setPayStatus(PayStatusEnum.PAID.getCode());
            commonOrderService.updateById(commonOrderEntity);

            // 子订单
            List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));
            commonOrderItemEntities.forEach(commonOrderItemEntity -> {
                commonOrderItemEntity.setStatus(OrderUtils.getCodeByPayStatus(PayStatusEnum.PAID.getCode()));
                commonOrderItemEntity.setPayStatus(PayStatusEnum.PAID.getCode());
            });
            commonOrderItemService.updateBatchById(commonOrderItemEntities);
        }
    }

    public List<CommonOrderItemEntity> getOrderItemEntities(String batchSn, String sn) {
        List<CommonOrderEntity> commonOrderEntities;

        if (StringUtils.hasLength(batchSn)) {
            commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", batchSn));
        } else {
            // 拆单支付
            commonOrderEntities = Stream.of(commonOrderService.getOne(new QueryWrapper<CommonOrderEntity>().eq("sn", sn), false)).collect(Collectors.toList());
        }

        List<CommonOrderItemEntity> commonOrderItemEntities = new ArrayList<>();
        for (CommonOrderEntity commonOrderEntity : commonOrderEntities) {
            commonOrderItemEntities.addAll(
                commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()))
            );
        }

        return commonOrderItemEntities;
    }
}
