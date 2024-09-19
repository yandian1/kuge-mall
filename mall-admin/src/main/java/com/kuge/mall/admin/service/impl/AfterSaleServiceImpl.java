package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.wechat.pay.java.service.refund.model.Status;
import com.kuge.mall.admin.service.AfterSaleService;
import com.kuge.mall.admin.utils.WxPayUtils;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.constant.AfterSaleStatusEnum;
import com.kuge.mall.common.constant.OrderStatusEnum;
import com.kuge.mall.common.entity.CommonAfterSaleEntity;
import com.kuge.mall.common.entity.CommonOrderEntity;
import com.kuge.mall.common.entity.CommonOrderItemEntity;
import com.kuge.mall.common.service.CommonAfterSaleService;
import com.kuge.mall.common.service.CommonOrderItemService;
import com.kuge.mall.common.service.CommonOrderService;
import com.kuge.mall.common.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/21
 */
@Service("afterSaleService")
public class AfterSaleServiceImpl implements AfterSaleService {

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Resource
    private CommonAfterSaleService commonAfterSaleService;

    @Resource
    private WxPayUtils wxPayUtils;

    @Override
    public R<PageData<AfterSalePageResVo>> pageList(AfterSalePageReqVo afterSalePageReqVo) {
        QueryWrapper<CommonAfterSaleEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");

        // 手机号
        String phone = afterSalePageReqVo.getPhone();
        if (StringUtils.hasLength(phone)) {
            wrapper.like("receiver_phone", phone);
        }

        // 订单号
        String orderNo = afterSalePageReqVo.getOrderNo();
        if (StringUtils.hasLength(orderNo)) {
            wrapper.like("order_no", orderNo);
        }

        // 商品名称
        String goodsName = afterSalePageReqVo.getGoodsName();
        if (StringUtils.hasLength(goodsName)) {
            wrapper.like("sku_name", goodsName);
        }

        // 售后状态
        String status = afterSalePageReqVo.getStatus();
        if (StringUtils.hasLength(status)) {
            wrapper.eq("status", status);
        }

        Page<CommonAfterSaleEntity> res = commonAfterSaleService.page(new Page<>(afterSalePageReqVo.getPageNum(), afterSalePageReqVo.getPageSize()), wrapper);

        PageData<AfterSalePageResVo> pageData = PageData.getPageData(res.getCurrent(), res.getSize(), res.getTotal(), res.getRecords().stream().map(record -> {
            AfterSalePageResVo afterSalePageResVo = new AfterSalePageResVo();
            BeanUtils.copyProperties(record, afterSalePageResVo);
            afterSalePageResVo.setStatusDesc(AfterSaleUtils.getStatusDesc(record.getStatus()));
            return afterSalePageResVo;
        }).collect(Collectors.toList()));

        return R.success(pageData);
    }

    @Override
    public R<AfterSaleVo> getAfterSale(Long id) {
        CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getById(id);
        if (commonAfterSaleEntity == null) {
            throw new CustomException("售后订单不存在");
        }

        AfterSaleVo afterSaleVo = new AfterSaleVo();
        BeanUtils.copyProperties(commonAfterSaleEntity, afterSaleVo);
        afterSaleVo.setStatusDesc(AfterSaleUtils.getStatusDesc(commonAfterSaleEntity.getStatus()));
        afterSaleVo.setSkuAttrs(GoodsUtils.getAttrValues(commonAfterSaleEntity.getSkuAttrs()));

        return R.success(afterSaleVo);
    }

    @Override
    public R<Void> updateAfterSale(AfterSaleUpdateVo reqVo) {
        CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getById(reqVo.getId());
        if (commonAfterSaleEntity == null) {
            throw new CustomException("订单不存在");
        }

        String status = reqVo.getStatus();

        // 拒绝
        if (Objects.equals(AfterSaleStatusEnum.REJECTED.getCode(), status)) {
            commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.REJECTED.getCode());
            if (!commonAfterSaleService.updateById(commonAfterSaleEntity)) {
                return R.fail("更新状态失败");
            }
            return R.success();
        }

        // 同意
        if (Objects.equals(AfterSaleStatusEnum.AGREED.getCode(), status)) {
            Long orderId = commonAfterSaleEntity.getOrderId();
            CommonOrderEntity commonOrderEntity = commonOrderService.getById(orderId);
            Integer batchPay = commonOrderEntity.getBatchPay();
            List<CommonOrderItemEntity> commonOrderItemEntities = new ArrayList<>();
            String outTradeNo = null;

            // 计算订单总价格，微信退款接口需要
            if (batchPay == 1) {
                // 批量支付
                String batchSn = outTradeNo = commonOrderEntity.getBatchSn();
                List<CommonOrderEntity> commonOrderEntities = commonOrderService.list(new QueryWrapper<CommonOrderEntity>().eq("batch_sn", batchSn));
                commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().in("order_id", commonOrderEntities.stream().map(CommonOrderEntity::getId).collect(Collectors.toList())));
            } else if (batchPay == 0) {
                // 单独支付
                outTradeNo = commonOrderEntity.getSn();
                commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().in("order_id", commonOrderEntity.getId()));
            }

            if (!CollectionUtils.isEmpty(commonOrderItemEntities)) {
                BigDecimal actualAmount = reqVo.getActualAmount();
                Status refundStatus = wxPayUtils.applyRefund(outTradeNo, commonAfterSaleEntity.getSn(), OrderUtils.calcTotalActualAmount(commonOrderItemEntities), actualAmount);
                if (refundStatus == Status.SUCCESS) {
                    commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.REFUNDED.getCode());
                    commonAfterSaleEntity.setApplyAmount(actualAmount);
                    return commonAfterSaleService.updateById(commonAfterSaleEntity) ? R.success() : R.fail("更新状态失败");
                } else if (refundStatus == Status.PROCESSING) {
                    commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.REFUNDING.getCode());
                    commonAfterSaleEntity.setApplyAmount(actualAmount);
                    return commonAfterSaleService.updateById(commonAfterSaleEntity) ? R.success() : R.fail("更新状态失败");
                } else {
                    return R.fail();
                }
            } else {
                return R.fail();
            }
        }

        return R.fail();
    }

    @Override
    public void notify(HttpServletRequest request) {
        RefundNotification refundNotification = wxPayUtils.parseRefundNotify(request);
        Status refundStatus = refundNotification.getRefundStatus();
        CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getOne(new QueryWrapper<CommonAfterSaleEntity>().eq("sn", refundNotification.getOutRefundNo()), false);

        if (refundStatus == Status.SUCCESS) {
            commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.REFUNDED.getCode());
            commonAfterSaleService.updateById(commonAfterSaleEntity);

            Long orderItemId = commonAfterSaleEntity.getOrderItemId();
            CommonOrderItemEntity commonOrderItemEntity = commonOrderItemService.getById(orderItemId);
            commonOrderItemEntity.setStatus(OrderStatusEnum.AFTER_SALE_REFUNDED.getCode());
            commonOrderItemService.updateById(commonOrderItemEntity);
        } else if (refundStatus == Status.PROCESSING) {
            commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.REFUNDING.getCode());
            commonAfterSaleService.updateById(commonAfterSaleEntity);
        }
    }
}
