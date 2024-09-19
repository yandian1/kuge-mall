package com.kuge.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.dto.CartSkuDto;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.*;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.*;
import com.kuge.mall.order.component.CancelOrderSender;
import com.kuge.mall.order.config.OrderConfig;
import com.kuge.mall.order.feign.CartRedisFeignService;
import com.kuge.mall.order.pojo.SkuNumPojo;
import com.kuge.mall.order.service.OrderService;
import com.kuge.mall.order.vo.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/21
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private CommonSkuService commonSkuService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private CommonOrderService commonOrderService;

    @Resource
    private CommonOrderItemService commonOrderItemService;

    @Resource
    private CommonShopService commonShopService;

    @Resource
    private CommonAddressService commonAddressService;

    @Resource
    private CommonCouponService commonCouponService;

    @Resource
    private CommonMemberCouponService commonMemberCouponService;

    @Resource
    private CommonFreightService commonFreightService;

    @Resource
    private CommonCartService commonCartService;

    @Resource
    private CancelOrderSender cancelOrderSender;

    @Resource
    private CommonAfterSaleService commonAfterSaleService;

    @Resource
    private CartRedisFeignService cartRedisFeignService;

    @Resource
    private OrderConfig orderConfig;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public R<String> createOrder(CreateOrderReqVo createOrderReqVo, HttpServletRequest request) {
        // 一个店铺对应一个母订单
        // 一个母订单对应多个商品

        List<CreateOrderReqVo.Shop> shopList = createOrderReqVo.getShopList();
        SnowflakeDistributeId idWorker = new SnowflakeDistributeId(0, 0);
        String batchSn = String.valueOf(idWorker.nextId());
        TokenDto tokenDto = JwtUtils.parseToken(request);
        Long memberId = tokenDto.getId();

        for (CreateOrderReqVo.Shop shop : shopList) {
            // 订单
            CommonOrderEntity commonOrderEntity = new CommonOrderEntity();
            commonOrderEntity.setMemberId(memberId);
            commonOrderEntity.setShopId(shop.getId());
            commonOrderEntity.setSn(String.valueOf(idWorker.nextId()));
            commonOrderEntity.setBatchSn(batchSn);
            commonOrderEntity.setStatus(OrderStatusEnum.UN_PAY.getCode());
            commonOrderEntity.setPayStatus(PayStatusEnum.UN_PAY.getCode());
            commonOrderEntity.setMessage(createOrderReqVo.getMessage());
            CommonAddressEntity commonAddressEntity = commonAddressService.getById(createOrderReqVo.getAddressId());
            commonOrderEntity.setReceiverName(commonAddressEntity.getName());
            commonOrderEntity.setReceiverPhone(commonAddressEntity.getPhone());
            commonOrderEntity.setReceiverAddress(commonAddressEntity.getProvince() + commonAddressEntity.getCity() + commonAddressEntity.getCounty() + commonAddressEntity.getDetail());

            if (!commonOrderService.save(commonOrderEntity)) {
                throw new CustomException("创建订单失败");
            }

            // 商品
            List<CommonOrderItemEntity> commonOrderItemEntities = shop.getGoodsList().stream().map(goods -> {

                CommonSkuEntity commonSkuEntity = commonSkuService.getById(goods.getSkuId());
                if (commonSkuEntity.getStock() - goods.getSkuNum() < 0) {
                    throw new CustomException(commonSkuEntity.getName() + "库存不足");
                }

                // 减库存
                reduceStock(commonSkuEntity, goods.getSkuNum());

                CommonOrderItemEntity commonOrderItemEntity = new CommonOrderItemEntity();
                commonOrderItemEntity.setOrderId(commonOrderEntity.getId());
                commonOrderItemEntity.setMemberId(memberId);
                commonOrderItemEntity.setShopId(shop.getId());
                commonOrderItemEntity.setSpuId(goods.getSpuId());
                commonOrderItemEntity.setSkuId(goods.getSkuId());
                commonOrderItemEntity.setSkuNum(goods.getSkuNum());
                commonOrderItemEntity.setSkuName(commonSkuEntity.getName());
                commonOrderItemEntity.setSkuAttrs(commonSkuEntity.getAttrs());
                commonOrderItemEntity.setSkuImg(commonSkuEntity.getImg());
                commonOrderItemEntity.setSkuPrice(commonSkuEntity.getPrice());

                // 商品金额
                BigDecimal goodsAmount = OrderUtils.calcGoodsAmount(commonSkuEntity.getPrice(), goods.getSkuNum());
                commonOrderItemEntity.setGoodsAmount(goodsAmount);

                // 运费
                BigDecimal deliveryAmount = OrderUtils.calcDeliveryAmount(commonSkuEntity, commonFreightService.getById(commonSpuService.getById(goods.getSpuId()).getFreightId()), goods.getSkuNum());
                commonOrderItemEntity.setDeliveryAmount(deliveryAmount);

                // 计算优惠金额
                Long memberCouponId = goods.getMemberCouponId();
                if (memberCouponId != null && couponCanUse(memberId, memberCouponId)) {
                    Long couponId = commonMemberCouponService.getById(memberCouponId).getCouponId();
                    BigDecimal couponAmount = OrderUtils.calcCouponAmount(goodsAmount, commonCouponService.getById(couponId));
                    commonOrderItemEntity.setCouponAmount(couponAmount);
                    commonOrderItemEntity.setMemberCouponId(memberCouponId);

                    CommonMemberCouponEntity commonMemberCouponEntity = commonMemberCouponService.getById(memberCouponId);
                    commonMemberCouponEntity.setStatus(MemberCouponStatusEnum.USED.getCode());
                    if (!commonMemberCouponService.updateById(commonMemberCouponEntity)) {
                        throw new CustomException("创建订单失败");
                    }
                } else {
                    commonOrderItemEntity.setCouponAmount(PriceUtils.precision(BigDecimal.ZERO));
                }

                return commonOrderItemEntity;
            }).collect(Collectors.toList());


            if (!commonOrderItemService.saveBatch(commonOrderItemEntities)) {
                throw new CustomException("创建订单失败");
            }

            cancelOrderSender.sendMessage(commonOrderEntity.getId());
        }


        if (createOrderReqVo.getByCart()) {
            R<Void> voidR = cartRedisFeignService.delSelected();
            if (voidR.getCode() != RCodeConstant.SUCCESS) {
                new CustomException("创建订单失败");
            }
        }

        return R.successData(batchSn);
    }

    @Override
    public R<PageData<OrderPageItemVo>> pageList(OrderPageReqVo orderPageReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        QueryWrapper<CommonOrderEntity> wrapper = new QueryWrapper<CommonOrderEntity>().eq("member_id", tokenDto.getId());
        wrapper.orderByDesc("create_time");

        // 订单状态
        if (StringUtils.hasLength(orderPageReqVo.getStatus())) {
            wrapper.eq("status", orderPageReqVo.getStatus());
        }

        List<CommonOrderEntity> commonOrderEntities = commonOrderService.list(wrapper);

        String goodsName = orderPageReqVo.getGoodsName();
        List<OrderPageItemVo> orderPageItemVos = commonOrderEntities.stream().map(commonOrderEntity -> {
            List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(
                new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId())
            );

            if (StringUtils.hasLength(goodsName) && commonOrderItemEntities.stream().noneMatch(commonOrderItemEntity -> commonOrderItemEntity.getSkuName().contains(goodsName))) {
                return null;
            }

            OrderPageItemVo orderPageItemVo = new OrderPageItemVo();
            orderPageItemVo.setId(commonOrderEntity.getId());
            orderPageItemVo.setSn(commonOrderEntity.getSn());
            orderPageItemVo.setShopName(commonShopService.getById(commonOrderEntity.getShopId()).getName());
            orderPageItemVo.setPrice(OrderUtils.calcTotalActualAmount(commonOrderItemEntities));
            orderPageItemVo.setStatus(commonOrderEntity.getStatus());
            orderPageItemVo.setStatusDesc(OrderUtils.getStatusDesc(commonOrderEntity.getStatus()));
            orderPageItemVo.setOrderItems(
                commonOrderItemEntities.stream().map(commonOrderItemEntity -> {
                    OrderPageItemVo.OrderItem orderItem = new OrderPageItemVo.OrderItem();
                    orderItem.setName(commonOrderItemEntity.getSkuName());
                    orderItem.setNum(commonOrderItemEntity.getSkuNum());
                    orderItem.setImg(commonOrderItemEntity.getSkuImg());
                    orderItem.setPrice(commonOrderItemEntity.getSkuPrice());
                    orderItem.setAttrs(GoodsUtils.getAttrValues(commonOrderItemEntity.getSkuAttrs()));
                    return orderItem;
                }).collect(Collectors.toList())
            );
            if (Objects.equals(commonOrderEntity.getStatus(), OrderStatusEnum.UN_PAY.getCode())) {
                long surplus = getSurplus(commonOrderEntity.getCreateTime());
                orderPageItemVo.setSurplus(surplus);
            }

            return orderPageItemVo;

        }).filter(Objects::nonNull).collect(Collectors.toList());

        PageData<OrderPageItemVo> pageData = PageData.getPageData(orderPageReqVo.getPageNum(), orderPageReqVo.getPageSize(), orderPageItemVos);
        return R.success(pageData);
    }

    @Override
    public R<OrderDetailVo> getOrder(Long id) {
        // 订单不存在
        CommonOrderEntity commonOrderEntity = commonOrderService.getById(id);
        if (commonOrderEntity == null) {
            throw new CustomException("订单不存在");
        }

        List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", commonOrderEntity.getId()));
        List<OrderDetailVo.OrderItem> orderItems = commonOrderItemEntities.stream().map(commonOrderItemEntity -> {
            OrderDetailVo.OrderItem orderItem = new OrderDetailVo.OrderItem();
            orderItem.setId(commonOrderItemEntity.getId());
            orderItem.setSpuId(commonOrderItemEntity.getSpuId());
            orderItem.setNum(commonOrderItemEntity.getSkuNum());
            orderItem.setAttrs(GoodsUtils.getAttrValues(commonOrderItemEntity.getSkuAttrs()));
            orderItem.setName(commonOrderItemEntity.getSkuName());
            orderItem.setImg(commonOrderItemEntity.getSkuImg());
            orderItem.setPrice(commonOrderItemEntity.getSkuPrice());
            orderItem.setStatus(commonOrderItemEntity.getStatus());
            orderItem.setStatusDesc(OrderUtils.getStatusDesc(commonOrderItemEntity.getStatus()));


            QueryWrapper<CommonAfterSaleEntity> afterSaleQuery = new QueryWrapper<CommonAfterSaleEntity>().eq("order_item_id", commonOrderItemEntity.getId());
            afterSaleQuery.orderByDesc("create_time");
            CommonAfterSaleEntity commonAfterSaleEntity = commonAfterSaleService.getOne(afterSaleQuery, false);
            // 存在售后
            if (commonAfterSaleEntity != null) {
                String afterSaleStatus = commonAfterSaleEntity.getStatus();

                // 售后中
                if (
                    Objects.equals(AfterSaleStatusEnum.PENDING.getCode(), afterSaleStatus)
                    || Objects.equals(AfterSaleStatusEnum.REFUNDING.getCode(), afterSaleStatus)
                ) {
                    orderItem.setStatus(OrderStatusEnum.AFTER_SALE_ING.getCode());
                    orderItem.setStatusDesc(OrderStatusEnum.AFTER_SALE_ING.getMsg());
                } else if (Objects.equals(AfterSaleStatusEnum.REFUNDED.getCode(), afterSaleStatus)) {
                    // 售后结束-已退款
                    orderItem.setStatus(OrderStatusEnum.AFTER_SALE_REFUNDED.getCode());
                    orderItem.setStatusDesc(OrderStatusEnum.AFTER_SALE_REFUNDED.getMsg());
                }
            }


            return orderItem;
        }).collect(Collectors.toList());


        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setId(id);
        orderDetailVo.setSn(commonOrderEntity.getSn());
        orderDetailVo.setShopName(commonShopService.getById(commonOrderEntity.getShopId()).getName());
        orderDetailVo.setPrice(OrderUtils.calcTotalActualAmount(commonOrderItemEntities));
        orderDetailVo.setStatus(commonOrderEntity.getStatus());
        orderDetailVo.setStatusDesc(OrderUtils.getStatusDesc(commonOrderEntity.getStatus()));
        orderDetailVo.setReceiverName(commonOrderEntity.getReceiverName());
        orderDetailVo.setReceiverPhone(commonOrderEntity.getReceiverPhone());
        orderDetailVo.setReceiverAddress(commonOrderEntity.getReceiverAddress());
        orderDetailVo.setCreateTime(commonOrderEntity.getCreateTime());
        orderDetailVo.setOrderItems(orderItems);

        if (Objects.equals(commonOrderEntity.getStatus(), OrderStatusEnum.UN_PAY.getCode())) {
            long surplus = getSurplus(commonOrderEntity.getCreateTime());
            orderDetailVo.setSurplus(surplus);
        }

        return R.success(orderDetailVo);
    }

    @Override
    public R<Void> cancelOrder(Long orderId) {
        CommonOrderEntity commonOrderEntity = commonOrderService.getById(orderId);
        if (commonOrderEntity == null) {
            return R.fail("未查询到订单");
        }

        // 如果未支付，则取消订单
        if (Objects.equals(commonOrderEntity.getPayStatus(), OrderStatusEnum.UN_PAY.getCode())) {
            // 恢复库存
            List<CommonOrderItemEntity> commonOrderItemEntities = commonOrderItemService.list(new QueryWrapper<CommonOrderItemEntity>().eq("order_id", orderId));
            if (!CollectionUtils.isEmpty(commonOrderItemEntities)) {
                commonSkuService.updateBatchById(
                    commonOrderItemEntities.stream().map(commonOrderItemEntity -> {
                        CommonSkuEntity commonSkuEntity = commonSkuService.getById(commonOrderItemEntity.getSkuId());
                        commonSkuEntity.setStock(commonSkuEntity.getStock() + commonOrderItemEntity.getSkuNum());
                        return commonSkuEntity;
                    }).collect(Collectors.toList())
                );

            }

            // 将订单设置为已取消
            commonOrderEntity.setStatus(OrderStatusEnum.CANCELLED.getCode());
            if (commonOrderService.updateById(commonOrderEntity)) {
                return R.success();
            } else {
                return R.fail("取消订单失败");
            }
        }

        return R.fail();
    }

    @Override
    public R<Integer> unPayCount(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        Integer count = (int)commonOrderService.count(
            new QueryWrapper<CommonOrderEntity>()
                .eq("member_id", tokenDto.getId())
                .eq("status", OrderStatusEnum.UN_PAY.getCode())
        );
        return R.success(count);
    }

    @Override
    public R<ConfirmOrderResVo> getConfirmInfo(ConfirmOrderReqVo confirmOrderReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        Long memberId = tokenDto.getId();

        ConfirmOrderResVo confirmOrderResVo = new ConfirmOrderResVo();
        List<CommonSkuEntity> commonSkuEntities;
        List<CommonShopEntity> commonShopEntities;
        List<SkuNumPojo> skuNumPojos;

        if (confirmOrderReqVo.getByCart()) {


            List<CartSkuDto> cartSkuDtos = cartRedisFeignService.getCartSkus().getData();

            if (CollectionUtils.isEmpty(cartSkuDtos)) {
                throw new CustomException("购物车无选中商品");
            }

            // 商品
            commonSkuEntities = commonSkuService.listByIds(cartSkuDtos.stream().map(CartSkuDto::getSkuId).collect(Collectors.toList()));

            // 店铺
            List<Long> shopIds = commonSkuEntities.stream().map(CommonSkuEntity::getShopId).collect(Collectors.toList());
            commonShopEntities = commonShopService.listByIds(shopIds);

            // 商品数量
            skuNumPojos = cartSkuDtos.stream().map(cartSkuDto -> {
                SkuNumPojo skuNumPojo = new SkuNumPojo();
                skuNumPojo.setSkuId(cartSkuDto.getSkuId());
                skuNumPojo.setNum(cartSkuDto.getNum());
                return skuNumPojo;
            }).collect(Collectors.toList());

        }
        else {
            // 根据商品信息生成订单数据
            Long skuId = confirmOrderReqVo.getId();
            Integer skuNum = confirmOrderReqVo.getNum();
            if (skuId == null) {
                throw new CustomException("请输入商品id");
            }
            if (skuNum == null) {
                throw new CustomException("请输入商品商品数量");
            }
            if (skuNum < 1) {
                throw new CustomException("商品数量至少为1");
            }
            // sku 不存在
            CommonSkuEntity commonSkuEntity = commonSkuService.getById(skuId);
            if (commonSkuEntity == null) {
                throw new CustomException("商品不存在");
            }
            // sku 库存不足
            if (commonSkuEntity.getStock() < skuNum) {
                throw new CustomException("商品库存不足");
            }
            // spu 不存在
            CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSkuEntity.getSpuId());
            if (commonSpuEntity == null) {
                throw new CustomException("商品不存在");
            }
            // 商品未上架
            if (commonSpuEntity.getStatus() != 1) {
                throw new CustomException("商品未上架");
            }


            SkuNumPojo skuNumPojo = new SkuNumPojo();
            skuNumPojo.setSkuId(commonSkuEntity.getId());
            skuNumPojo.setNum(confirmOrderReqVo.getNum());
            skuNumPojos = Collections.singletonList(skuNumPojo);
            commonSkuEntities = Collections.singletonList(commonSkuEntity);
            commonShopEntities = Collections.singletonList(commonShopService.getById(commonSkuEntity.getShopId()));

        }

        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            ConfirmOrderResVo.Address address = caclConfirmOrderAddress(confirmOrderReqVo);
            confirmOrderResVo.setAddress(address);
        });

        List<CommonShopEntity> finalCommonShopEntities = commonShopEntities;
        List<CommonSkuEntity> finalCommonSkuEntities = commonSkuEntities;
        List<SkuNumPojo> finalSkuNumPojos = skuNumPojos;
        CompletableFuture<Void> shopFuture = CompletableFuture.runAsync(() -> {
            Map<String, Object> goodsInfo = calcShopInfo(finalCommonShopEntities, finalCommonSkuEntities, finalSkuNumPojos, confirmOrderReqVo.getCouponList(), memberId);
            confirmOrderResVo.setGoodsPrice((BigDecimal) goodsInfo.get("goodsPrice"));
            confirmOrderResVo.setDeliveryPrice((BigDecimal) goodsInfo.get("deliveryPrice"));
            confirmOrderResVo.setCouponPrice((BigDecimal) goodsInfo.get("couponPrice"));
            confirmOrderResVo.setActualPrice((BigDecimal) goodsInfo.get("actualPrice"));
            confirmOrderResVo.setShopList((List<ConfirmOrderResVo.ShopItem>) goodsInfo.get("shopList"));
        });

        CompletableFuture.allOf(addressFuture, shopFuture).join();

        return R.success(confirmOrderResVo);
    }

    private Map<String, Object> calcShopInfo(List<CommonShopEntity> commonShopEntities, List<CommonSkuEntity> commonSkuEntities, List<SkuNumPojo> skuNumPojos, List<ConfirmOrderReqVo.Coupon> selectedCouponList, Long memberId) {
        List<BigDecimal> goodsAmountList = new ArrayList<>();
        List<BigDecimal> couponAmountList = new ArrayList<>();
        List<BigDecimal> deliveryAmountList = new ArrayList<>();
        List<ConfirmOrderResVo.Coupon> goodsCouponList = new ArrayList<>();


        List<ConfirmOrderResVo.ShopItem> shopList = commonShopEntities.stream().map(commonShopEntity -> {
            ConfirmOrderResVo.ShopItem shopItem = new ConfirmOrderResVo.ShopItem();
            shopItem.setName(commonShopEntity.getName());
            shopItem.setId(commonShopEntity.getId());
            shopItem.setGoodsList(
                commonSkuEntities.stream()
                    .filter(commonSkuEntity -> Objects.equals(commonShopEntity.getId(), commonSkuEntity.getShopId()))
                    .map(commonSkuEntity -> {
                        CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSkuEntity.getSpuId());
                        SkuNumPojo skuNumPojo = skuNumPojos.stream().filter(pojo -> Objects.equals(pojo.getSkuId(), commonSkuEntity.getId())).findFirst().get();

                        // 库存不足
                        if (commonSkuEntity.getStock() < skuNumPojo.getNum()) {
                            throw new CustomException(commonSpuEntity.getName() + "库存不足");
                        }

                        ConfirmOrderResVo.Goods goods = new ConfirmOrderResVo.Goods();
                        goods.setSkuId(commonSkuEntity.getId());
                        goods.setNum(skuNumPojo.getNum());
                        goods.setAttrs(GoodsUtils.getAttrValues(commonSkuEntity.getAttrs()));
                        goods.setPrice(commonSkuEntity.getPrice());
                        if (commonSpuEntity != null) {
                            goods.setSpuId(commonSpuEntity.getId());
                            goods.setImg(commonSpuEntity.getFirstImg());
                            goods.setName(commonSpuEntity.getName());
                        }

                        // 商品金额
                        BigDecimal goodsAmount = OrderUtils.calcGoodsAmount(commonSkuEntity.getPrice(), skuNumPojo.getNum());
                        goodsAmountList.add(goodsAmount);

                        // 运费
                        BigDecimal deliveryAmount = OrderUtils.calcDeliveryAmount(commonSkuEntity, commonFreightService.getById(commonSpuEntity.getFreightId()), skuNumPojo.getNum());
                        deliveryAmountList.add(deliveryAmount);


                        // 可用优惠券
                        List<ConfirmOrderResVo.Coupon> couponList = genCouponList(memberId, goodsAmount);
                        List<ConfirmOrderResVo.Coupon> newCouponList = new ArrayList<>();

                        for (ConfirmOrderResVo.Coupon coupon : couponList) {
                            if (
                                goodsCouponList.stream().noneMatch(goodsCoupon -> Objects.equals(goodsCoupon.getMemberCouponId(), coupon.getMemberCouponId()))
                            ) {
                                newCouponList.add(coupon);
                                goodsCouponList.add(coupon);
                            }
                        }

                        goods.setCouponList(newCouponList);

                        // 计算优惠金额
                        if (!CollectionUtils.isEmpty(selectedCouponList)) {
                            ConfirmOrderReqVo.Coupon selectedCoupon = selectedCouponList.stream().filter(coupon -> Objects.equals(coupon.getSkuId(), commonSkuEntity.getId())).findFirst().orElse(null);

                            if (selectedCoupon != null && couponCanUse(memberId, selectedCoupon.getMemberCouponId())) {
                                Long couponId = commonMemberCouponService.getById(selectedCoupon.getMemberCouponId()).getCouponId();
                                BigDecimal couponAmount = OrderUtils.calcCouponAmount(goodsAmount, commonCouponService.getById(couponId));
                                couponAmountList.add(couponAmount);
                                goods.setCouponPrice(couponAmount);
                                goods.setMemberCouponId(selectedCoupon.getMemberCouponId());
                            } else {
                                goods.setMemberCouponId(null);
                                couponAmountList.add(PriceUtils.precision(BigDecimal.ZERO));
                            }
                        } else {
                            goods.setMemberCouponId(null);
                            couponAmountList.add(PriceUtils.precision(BigDecimal.ZERO));
                        }


                        return goods;
                    }).collect(Collectors.toList())
            );
            return shopItem;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();

        result.put("goodsPrice", PriceUtils.precision(goodsAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));
        result.put("deliveryPrice", PriceUtils.precision(deliveryAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));
        result.put("couponPrice", PriceUtils.precision(couponAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));
        result.put("actualPrice", OrderUtils.calcTotalActualAmount(goodsAmountList, deliveryAmountList, couponAmountList));
        result.put("shopList", shopList);
        
        return result;
    }

    private long getSurplus(LocalDateTime createTime) {
        long surplus = orderConfig.getDelayTimes() - (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - createTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        return surplus > 0 ? surplus : 0;
    }

    private List<ConfirmOrderResVo.Coupon> genCouponList(Long memberId, BigDecimal goodsAmount) {

        List<CommonMemberCouponEntity> commonMemberCouponEntities = commonMemberCouponService.list(new QueryWrapper<CommonMemberCouponEntity>().eq("member_id", memberId));
        List<ConfirmOrderResVo.Coupon> couponList = new ArrayList<>();

        if (CollectionUtils.isEmpty(commonMemberCouponEntities)) {
            return couponList;
        }

        for (CommonMemberCouponEntity commonMemberCouponEntity : commonMemberCouponEntities) {
            CommonCouponEntity commonCouponEntity = commonCouponService.getById(commonMemberCouponEntity.getCouponId());
            LocalDateTime startTime = commonCouponEntity.getStartTime();
            LocalDateTime endTime = commonCouponEntity.getEndTime();
            LocalDateTime now = LocalDateTime.now();

            // 未使用
            if (
                (now.isEqual(startTime) || now.isAfter(startTime))
                    && (now.isEqual(endTime) || now.isBefore(endTime))
                    && Objects.equals(commonMemberCouponEntity.getStatus(), MemberCouponStatusEnum.UNUSE.getCode())
            ) {
                BigDecimal couponAmount = OrderUtils.calcCouponAmount(goodsAmount, commonCouponService.getById(commonMemberCouponEntity.getCouponId()));
                // 优惠金额大于0
                if (couponAmount.compareTo(BigDecimal.ZERO) > 0) {
                    ConfirmOrderResVo.Coupon coupon = new ConfirmOrderResVo.Coupon();
                    BeanUtils.copyProperties(commonCouponEntity, coupon);
                    coupon.setTypeDesc(CouponUtils.getTypeDesc(coupon.getType()));
                    coupon.setCouponId(commonCouponEntity.getId());
                    coupon.setMemberCouponId(commonMemberCouponEntity.getId());
                    couponList.add(coupon);
                }
            }
        }
        return couponList;
    }

    private boolean couponCanUse(Long memberId, Long memberCouponId) {
        CommonMemberCouponEntity commonMemberCouponEntity = commonMemberCouponService.getById(memberCouponId);
        if (commonMemberCouponEntity == null) {
            throw new CustomException("优惠卷不存在");
        }

        if (!Objects.equals(commonMemberCouponEntity.getMemberId(), memberId)) {
            throw new CustomException("用户没有该优惠券");
        }

        CommonCouponEntity commonCouponEntity = commonCouponService.getById(commonMemberCouponEntity.getCouponId());
        if (commonCouponEntity == null) {
            throw new CustomException("优惠卷不存在");
        }

        LocalDateTime startTime = commonCouponEntity.getStartTime();
        LocalDateTime endTime = commonCouponEntity.getEndTime();
        String name = commonCouponEntity.getName();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            throw new CustomException("优惠卷" + name + "未到使用时间");
        } else if (now.isAfter(endTime)) {
            throw new CustomException("优惠卷" + name + "已过期");
        } else {
            if (Objects.equals(commonMemberCouponEntity.getStatus(), MemberCouponStatusEnum.USED.getCode())) {
                throw new CustomException("优惠券已使用");
            }
        }

        return true;
    }

    private void reduceStock(CommonSkuEntity commonSkuEntity, Integer reduce) {
        RLock rLock = redissonClient.getLock("reduceStock");
        try {
            boolean isLocked = rLock.tryLock(3, TimeUnit.MILLISECONDS);
            if (isLocked) {
                commonSkuEntity.setStock(commonSkuEntity.getStock() - reduce);
                commonSkuService.updateById(commonSkuEntity);
            }
        } catch (Exception e) {
            rLock.unlock();
        }
    }

    private ConfirmOrderResVo.Address caclConfirmOrderAddress(ConfirmOrderReqVo confirmOrderReqVo) {
        Long addressId = confirmOrderReqVo.getAddressId();
        if (addressId != null) {
            ConfirmOrderResVo.Address address = new ConfirmOrderResVo.Address();
            CommonAddressEntity commonAddressEntity = commonAddressService.getById(addressId);
            BeanUtils.copyProperties(commonAddressEntity, address);
            address.setIsDefault(commonAddressEntity.getIsDefault() == 1);
            return address;
        } else {
            List<CommonAddressEntity> list = commonAddressService.list();
            if (!CollectionUtils.isEmpty(list)) {
                CommonAddressEntity commonAddressEntity = null;
                for (CommonAddressEntity item : list) {
                    if (item.getIsDefault() == 1) {
                        commonAddressEntity = item;
                        break;
                    }
                }
                commonAddressEntity = (commonAddressEntity == null ? list.get(0) : commonAddressEntity);
                ConfirmOrderResVo.Address address = new ConfirmOrderResVo.Address();
                BeanUtils.copyProperties(commonAddressEntity, address);
                address.setIsDefault(commonAddressEntity.getIsDefault() == 1);
                return address;
            } else {
                return null;
            }
        }
    }
}
