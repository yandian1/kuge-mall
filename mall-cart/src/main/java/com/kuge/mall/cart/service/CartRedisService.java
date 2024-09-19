package com.kuge.mall.cart.service;

import com.kuge.mall.cart.vo.CartAddVo;
import com.kuge.mall.cart.vo.CartInfoResVo;
import com.kuge.mall.cart.vo.ChangeQuantityReqVo;
import com.kuge.mall.cart.vo.ChangeSelectReqVo;
import com.kuge.mall.common.dto.CartSkuDto;
import com.kuge.mall.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
public interface CartRedisService {
    R<Void> add(CartAddVo cartAddVo, HttpServletRequest request);

    R<CartInfoResVo> info(HttpServletRequest request);

    R<Long> getCount(HttpServletRequest request);

    R<Long> getCountByMemberId(Long memberId);

    R<Void> del(Long shopId, Long skuId, HttpServletRequest request);

    R<Void> changeSelect(List<ChangeSelectReqVo> changeSelectReqVos, HttpServletRequest request);

    R<Void> changeQuantity(ChangeQuantityReqVo changeQuantityReqVo, HttpServletRequest request);

    R<Void> delSelected(HttpServletRequest request);

    R<List<CartSkuDto>> getCartSkus(HttpServletRequest request);
}

