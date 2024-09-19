package com.kuge.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.AfterSaleStatusEnum;
import com.kuge.mall.common.entity.CommonAfterSaleEntity;
import com.kuge.mall.common.entity.CommonOrderEntity;
import com.kuge.mall.common.entity.CommonOrderItemEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.service.CommonAfterSaleService;
import com.kuge.mall.common.service.CommonOrderItemService;
import com.kuge.mall.common.service.CommonOrderService;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.utils.*;
import com.kuge.mall.order.service.AfterSaleService;
import com.kuge.mall.order.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/21
 */
@Service("afterSaleService")
public class AfterSaleServiceImpl implements AfterSaleService {

    @Resource
    private CommonAfterSaleService commonAfterSaleService;

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Resource
    private CommonShopService commonShopService;

    @Override
    public R<Long> apply(AfterSaleAddVo afterSaleAddVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        CommonOrderItemEntity commonOrderItemEntity = commonOrderItemService.getById(afterSaleAddVo.getOrderItemId());

        // 订单存在售后
        String status = commonOrderItemEntity.getStatus();
        // if (Objects.equals(OrderStatusEnum.AFTER_SALE_PENDING.getCode(), status)) {
        //     throw new CustomException("该订单在售后处理中");
        // }
        // if (Objects.equals(OrderStatusEnum.AFTER_SALE_AGREED.getCode(), status)) {
        //     throw new CustomException("该订单已退款");
        // }

        CommonOrderEntity commonOrderEntity = commonOrderService.getById(commonOrderItemEntity.getOrderId());
        CommonAfterSaleEntity commonAfterSaleEntity = new CommonAfterSaleEntity();
        SnowflakeDistributeId idWorker = new SnowflakeDistributeId(0, 0);
        String sn = String.valueOf(idWorker.nextId());

        BeanUtils.copyProperties(afterSaleAddVo, commonAfterSaleEntity);
        commonAfterSaleEntity.setUserId(tokenDto.getId());
        commonAfterSaleEntity.setOrderId(commonOrderEntity.getId());
        commonAfterSaleEntity.setOrderItemId(commonOrderItemEntity.getId());
        commonAfterSaleEntity.setSn(sn);
        commonAfterSaleEntity.setApplyNum(afterSaleAddVo.getApplyNum());
        commonAfterSaleEntity.setSkuName(commonOrderItemEntity.getSkuName());
        commonAfterSaleEntity.setSkuPrice(commonOrderItemEntity.getSkuPrice());
        commonAfterSaleEntity.setSkuAttrs(commonOrderItemEntity.getSkuAttrs());
        commonAfterSaleEntity.setSkuImg(commonOrderItemEntity.getSkuImg());
        commonAfterSaleEntity.setSkuNum(commonOrderItemEntity.getSkuNum());
        commonAfterSaleEntity.setApplyAmount(PriceUtils.precision(commonOrderItemEntity.getSkuPrice().multiply(BigDecimal.valueOf(afterSaleAddVo.getApplyNum()))));
        commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.PENDING.getCode());

        if (!commonAfterSaleService.save(commonAfterSaleEntity)) {
            throw new CustomException("申请失败");
        }

        // commonOrderItemEntity.setStatus(OrderStatusEnum.AFTER_SALE_PENDING.getCode());
        if (!commonOrderItemService.updateById(commonOrderItemEntity)) {
            throw new CustomException("申请失败");
        }

        return R.success(commonAfterSaleEntity.getId());
    }

    @Override
    public R<AfterSaleVo> getAfterSale(Long id) {
        CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getById(id);
        if (commonAfterSaleEntity == null) {
            throw new CustomException("售后订单不存在");
        }

        Long orderId = commonAfterSaleEntity.getOrderId();
        CommonOrderEntity commonOrderEntity = commonOrderService.getById(orderId);
        CommonShopEntity commonShopEntity = commonShopService.getById(commonOrderEntity.getShopId());



        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setImg(commonAfterSaleEntity.getSkuImg());
        orderGoods.setName(commonAfterSaleEntity.getSkuName());
        orderGoods.setPrice(commonAfterSaleEntity.getSkuPrice());
        orderGoods.setNum(commonAfterSaleEntity.getSkuNum());
        orderGoods.setAttrs(GoodsUtils.getAttrValues(commonAfterSaleEntity.getSkuAttrs()));

        AfterSaleVo afterSaleVo = new AfterSaleVo();
        afterSaleVo.setShopName(commonShopEntity.getName());
        afterSaleVo.setStatusDesc(AfterSaleUtils.getStatusDesc(commonAfterSaleEntity.getStatus()));
        afterSaleVo.setOrderGoods(orderGoods);
        BeanUtils.copyProperties(commonAfterSaleEntity, afterSaleVo);

        return R.success(afterSaleVo);
    }

    @Override
    public R<PageData<AfterSalePageResVo>> pageList(AfterSalePageReqVo afterSalePageReqVo) {
        QueryWrapper<CommonAfterSaleEntity> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("create_time");

        // 商品名称
        String goodsName = afterSalePageReqVo.getGoodsName();
        if (StringUtils.hasLength(goodsName)) {
            wrapper.like("sku_name", goodsName);
        }

        // 审核状态
        String status = afterSalePageReqVo.getStatus();
        if (StringUtils.hasLength(status)) {
            if (status.equals("handled")) {
                wrapper.and(w -> {
                    w.eq("status", AfterSaleStatusEnum.AGREED.getCode()).or().eq("status", AfterSaleStatusEnum.REJECTED.getCode());
                });
            } else {
                wrapper.and(w -> {
                    w.eq("status", afterSalePageReqVo.getStatus());
                });
            }
        }

        Page<CommonAfterSaleEntity> page = commonAfterSaleService.page(new Page<>(afterSalePageReqVo.getPageNum(), afterSalePageReqVo.getPageSize()), wrapper);

        List<AfterSalePageResVo> afterSalePageResVos = page.getRecords().stream().map(commonAfterSaleEntity -> {
            Long orderId = commonAfterSaleEntity.getOrderId();
            CommonOrderEntity commonOrderEntity = commonOrderService.getById(orderId);
            CommonShopEntity commonShopEntity = commonShopService.getById(commonOrderEntity.getShopId());

            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setImg(commonAfterSaleEntity.getSkuImg());
            orderGoods.setName(commonAfterSaleEntity.getSkuName());
            orderGoods.setPrice(commonAfterSaleEntity.getSkuPrice());
            orderGoods.setAttrs(GoodsUtils.getAttrValues(commonAfterSaleEntity.getSkuAttrs()));
            orderGoods.setNum(commonAfterSaleEntity.getSkuNum());

            AfterSalePageResVo afterSalePageResVo = new AfterSalePageResVo();
            afterSalePageResVo.setStatus(commonAfterSaleEntity.getStatus());
            afterSalePageResVo.setStatusDesc(AfterSaleUtils.getStatusDesc(commonAfterSaleEntity.getStatus()));
            afterSalePageResVo.setShopName(commonShopEntity.getName());
            afterSalePageResVo.setOrderGoods(orderGoods);
            BeanUtils.copyProperties(commonAfterSaleEntity, afterSalePageResVo);

            return afterSalePageResVo;
        }).collect(Collectors.toList());


        PageData<AfterSalePageResVo> pageData = PageData.getPageData(page.getCurrent(), page.getSize(), page.getTotal(), afterSalePageResVos);

        return R.success(pageData);
    }

    @Override
    public R<Void> cancel(Long id) {
        CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getById(id);
        if (commonAfterSaleEntity == null) {
            throw new CustomException("售后订单不存在");
        }

        commonAfterSaleEntity.setStatus(AfterSaleStatusEnum.CANCELLED.getCode());

        if (!commonAfterSaleService.updateById(commonAfterSaleEntity)) {
            throw new CustomException("取消失败");
        }

        return R.success();
    }
}
