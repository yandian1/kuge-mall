package com.kuge.mall.cart.controller;

import com.kuge.mall.cart.vo.*;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.kuge.mall.cart.service.CartService;
import java.util.List;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody CartAddVo cartAddVo, HttpServletRequest request) {
        return cartService.add(cartAddVo, request);
    }

    @PostMapping("/info")
    public R<CartInfoResOldVo> info(HttpServletRequest request) {
        return cartService.info(request);
    }

    @PostMapping("/count")
    public R<Integer> getCount(HttpServletRequest request) {
        return cartService.getCount(request);
    }

    @PostMapping("/count/{memberId}")
    public R<Integer> getCountByMemberId(@PathVariable("memberId") Long memberId) {
        return cartService.getCountByMemberId(memberId);
    }

    @PostMapping("/delSelected")
    public R<Void> delSelected(HttpServletRequest request) {
        return cartService.delSelected(request);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return cartService.del(id);
    }

    @PostMapping("/changeSelect")
    public R<Void> changeSelect(@RequestBody List<ChangeSelectReqOldVo> changeSelectReqVos) {
        return cartService.changeSelect(changeSelectReqVos);
    }

    @PostMapping("/changeQuantity")
    public R<Void> changeQuantity(@Validated @RequestBody ChangeQuantityReqOldVo changeQuantityReqVo) {
        return cartService.changeQuantity(changeQuantityReqVo);
    }
}
