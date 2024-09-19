package com.kuge.mall.cart.controller;

import com.kuge.mall.cart.service.CartRedisService;
import com.kuge.mall.cart.vo.CartAddVo;
import com.kuge.mall.cart.vo.CartInfoResVo;
import com.kuge.mall.cart.vo.ChangeQuantityReqVo;
import com.kuge.mall.cart.vo.ChangeSelectReqVo;
import com.kuge.mall.common.dto.CartSkuDto;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
@RestController
@RequestMapping("/cart/redis")
public class CartRedisController {
    @Resource
    private CartRedisService cartRedisService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody CartAddVo cartAddVo, HttpServletRequest request) {
        return cartRedisService.add(cartAddVo, request);
    }

    @PostMapping("/info")
    public R<CartInfoResVo> info(HttpServletRequest request) {
        return cartRedisService.info(request);
    }

    @PostMapping("/count")
    public R<Long> getCount(HttpServletRequest request) {
        return cartRedisService.getCount(request);
    }

    @PostMapping("/count/{memberId}")
    public R<Long> getCountByMemberId(@PathVariable("memberId") Long memberId) {
        return cartRedisService.getCountByMemberId(memberId);
    }

    @PostMapping("/delSelected")
    public R<Void> delSelected(HttpServletRequest request) {
        return cartRedisService.delSelected(request);
    }

    @PostMapping("/del/{shopId}/{skuId}")
    public R<Void> del(@PathVariable("shopId") Long shopId, @PathVariable("skuId") Long skuId, HttpServletRequest request) {
        return cartRedisService.del(shopId, skuId, request);
    }

    @PostMapping("/changeSelect")
    public R<Void> changeSelect(@RequestBody List<ChangeSelectReqVo> changeSelectReqVos, HttpServletRequest request) {
        return cartRedisService.changeSelect(changeSelectReqVos, request);
    }

    @PostMapping("/changeQuantity")
    public R<Void> changeQuantity(@Validated @RequestBody ChangeQuantityReqVo changeQuantityReqVo, HttpServletRequest request) {
        return cartRedisService.changeQuantity(changeQuantityReqVo, request);
    }

    @PostMapping("/cartSkus")
    public R<List<CartSkuDto>> getCartSkus(HttpServletRequest request) {
        return cartRedisService.getCartSkus(request);
    }
}
