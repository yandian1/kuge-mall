package com.kuge.mall.common.utils;

import com.kuge.mall.common.constant.*;
import com.kuge.mall.common.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024/6/6
 */
public class OrderUtils {
    public static BigDecimal calcGoodsAmount(BigDecimal price, Integer num) {
        return PriceUtils.precision(BigDecimal.valueOf(num).multiply(price));
    }

    public static BigDecimal calcDeliveryAmount(CommonSkuEntity commonSkuEntity, CommonFreightEntity commonFreightEntity, Integer count) {
        return PriceUtils.precision(getDeliveryAmount(commonSkuEntity, commonFreightEntity, count));
    }

    public static BigDecimal calcCouponAmount(BigDecimal goodsAmount, CommonCouponEntity commonCouponEntity) {
        return PriceUtils.precision(getCouponAmount(goodsAmount, commonCouponEntity));
    }

    public static BigDecimal calcActualAmount(CommonOrderItemEntity commonOrderItemEntity) {
        BigDecimal actualAmount = commonOrderItemEntity.getGoodsAmount().add(commonOrderItemEntity.getDeliveryAmount()).subtract(commonOrderItemEntity.getCouponAmount());
        return PriceUtils.precision(actualAmount);
    }

    public static BigDecimal calcTotalActualAmount(List<CommonOrderItemEntity> commonOrderItemEntities) {
        List<BigDecimal> goodsAmountList = new ArrayList<>();
        List<BigDecimal> deliveryAmountList = new ArrayList<>();
        List<BigDecimal> couponAmountList = new ArrayList<>();

        commonOrderItemEntities.forEach(commonOrderItemEntity -> {
            goodsAmountList.add(commonOrderItemEntity.getGoodsAmount());
            deliveryAmountList.add(commonOrderItemEntity.getDeliveryAmount());
            couponAmountList.add(commonOrderItemEntity.getCouponAmount());
        });

        return calcTotalActualAmount(goodsAmountList, deliveryAmountList, couponAmountList);
    }

    public static BigDecimal calcTotalActualAmount(List<BigDecimal> goodsAmountList, List<BigDecimal> deliveryAmountList, List<BigDecimal> couponAmountList) {
        BigDecimal totalActualAmount = goodsAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(deliveryAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
            .subtract(couponAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        return PriceUtils.precision(totalActualAmount);
    }

    public static String getStatusDesc(String code) {

        if (Objects.equals(code, OrderStatusEnum.UN_PAY.getCode())) return OrderStatusEnum.UN_PAY.getMsg();


        if (Objects.equals(code, OrderStatusEnum.UN_SEND.getCode())) return OrderStatusEnum.UN_SEND.getMsg();


        if (Objects.equals(code, OrderStatusEnum.UN_RECEIVE.getCode())) return OrderStatusEnum.UN_RECEIVE.getMsg();

        if (Objects.equals(code, OrderStatusEnum.RECEIVED.getCode())) return OrderStatusEnum.RECEIVED.getMsg();

        // if (Objects.equals(code, OrderStatusEnum.AFTER_SALE_PENDING.getCode())) return OrderStatusEnum.AFTER_SALE_PENDING.getMsg();
        //
        //
        // if (Objects.equals(code, OrderStatusEnum.AFTER_SALE_AGREED.getCode())) return OrderStatusEnum.AFTER_SALE_AGREED.getMsg();
        //
        //
        // if (Objects.equals(code, OrderStatusEnum.AFTER_SALE_REJECTED.getCode())) return OrderStatusEnum.AFTER_SALE_REJECTED.getMsg();
        //
        //
        // if (Objects.equals(code, OrderStatusEnum.AFTER_SALE_CANCELLED.getCode())) return OrderStatusEnum.AFTER_SALE_CANCELLED.getMsg();


        if (Objects.equals(code, OrderStatusEnum.CANCELLED.getCode())) return OrderStatusEnum.CANCELLED.getMsg();


        if (Objects.equals(code, OrderStatusEnum.COMPLETED.getCode())) return OrderStatusEnum.COMPLETED.getMsg();

        return "";
    }

    public static String getCodeByPayStatus(String code) {

        if (Objects.equals(code, PayStatusEnum.UN_PAY.getCode())) return OrderStatusEnum.UN_PAY.getCode();

        if (Objects.equals(code, PayStatusEnum.PAID.getCode())) return OrderStatusEnum.UN_SEND.getCode();

        if (Objects.equals(code, PayStatusEnum.REFUNDED.getCode())) return OrderStatusEnum.CANCELLED.getCode();

        if (Objects.equals(code, PayStatusEnum.CLOSED.getCode())) return OrderStatusEnum.CANCELLED.getCode();

        return null;
    }

    private static BigDecimal getDeliveryAmount(CommonSkuEntity commonSkuEntity, CommonFreightEntity commonFreightEntity, Integer count) {
        String type = commonFreightEntity.getType();

        // 包邮
        if (Objects.equals(type, FreightFeeTypeEnum.FREE.getCode())) {
            return BigDecimal.ZERO;
        }

        // 按数量
        if (Objects.equals(type, FreightFeeTypeEnum.COUNT.getCode())) {
            if (count <= 0) {
                return BigDecimal.ZERO;
            }

            Integer firstCount = commonFreightEntity.getFirstCount();

            // 首件
            if (count <= firstCount) {
                return commonFreightEntity.getFirstCountFee();
            }

            BigDecimal fee = BigDecimal.ZERO;
            fee = fee.add(commonFreightEntity.getFirstCountFee());

            // 续件
            int integer = (count - firstCount) / commonFreightEntity.getContinueCount();
            int remainder = (count - firstCount) % commonFreightEntity.getContinueCount();

            if (integer > 0) {
                fee = fee.add(commonFreightEntity.getContinueCountFee().multiply(BigDecimal.valueOf(integer)));
            }

            if (remainder > 0) {
                fee = fee.add(commonFreightEntity.getContinueCountFee());
            }

            return fee;
        }

        // 按重量
        if (Objects.equals(type, FreightFeeTypeEnum.WEIGHT.getCode())) {
            double weight = count * commonSkuEntity.getWeight();

            if (weight <= 0) {
                return BigDecimal.ZERO;
            }

            double firstWeight = commonFreightEntity.getFirstWeight();

            // 首重
            if (weight <= firstWeight) {
                return commonFreightEntity.getFirstWeightFee();
            }

            BigDecimal fee = BigDecimal.ZERO;
            fee = fee.add(commonFreightEntity.getContinueWeightFee());

            // 续重
            double quotient = (weight - firstWeight) / commonFreightEntity.getContinueWeight();
            double integer = (int) quotient;
            double remainder = quotient - integer;

            if (integer > 0) {
                fee = fee.add(commonFreightEntity.getContinueWeightFee().multiply(BigDecimal.valueOf(integer)));
            }

            if (remainder > 0) {
                fee = fee.add(commonFreightEntity.getContinueWeightFee());
            }

            return fee;
        }

        return BigDecimal.ZERO;
    }

    private static BigDecimal getCouponAmount(BigDecimal goodsPrice, CommonCouponEntity commonCouponEntity) {
        String type = commonCouponEntity.getType();

        // 抵扣
        if (Objects.equals(type, CouponTypeEnum.DEDUCT.getCode())) {
            BigDecimal threshold = commonCouponEntity.getThreshold();
            BigDecimal deduct = commonCouponEntity.getDeduct();

            // 无使用门槛或者已经达到使用门槛，使用优惠券
            if (threshold == null || goodsPrice.compareTo(threshold) >= 0) {
                return goodsPrice.compareTo(deduct) > 0 ? deduct : goodsPrice;
            }

            // 有使用门槛 但未达到使用门槛
            return BigDecimal.ZERO;
        } else if (Objects.equals(type, CouponTypeEnum.DISCOUNT.getCode())) {
            // 折扣
            BigDecimal discount = commonCouponEntity.getDiscount();
            BigDecimal threshold = commonCouponEntity.getThreshold();

            // 无使用门槛或者已经达到使用门槛，使用优惠券
            if (threshold == null || goodsPrice.compareTo(threshold) >= 0) {
                BigDecimal discountPrice = goodsPrice.multiply(discount);
                return goodsPrice.compareTo(discountPrice) > 0 ? discountPrice : goodsPrice;
            }

            // 有使用门槛 但未达到使用门槛
            return BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }
}
