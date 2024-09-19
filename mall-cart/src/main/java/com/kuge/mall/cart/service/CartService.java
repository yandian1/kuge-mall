package com.kuge.mall.cart.service;

import com.kuge.mall.cart.vo.*;
import com.kuge.mall.common.utils.R;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
public interface CartService {
    R<Void> add(CartAddVo cartAddVo, HttpServletRequest request);

    R<CartInfoResOldVo> info(HttpServletRequest request);

    R<Void> changeSelect(List<ChangeSelectReqOldVo> changeSelectReqVos);

    R<Void> del(Long id);

    R<Void> changeQuantity(ChangeQuantityReqOldVo changeQuantityReqVos);

    R<Integer> getCount(HttpServletRequest request);

    R<Integer> getCountByMemberId(Long memberId);

    R<Void> delSelected(HttpServletRequest request);
}

