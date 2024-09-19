package com.kuge.mall.order.controller;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.AfterSaleService;
import com.kuge.mall.order.vo.AfterSaleAddVo;
import com.kuge.mall.order.vo.AfterSalePageReqVo;
import com.kuge.mall.order.vo.AfterSalePageResVo;
import com.kuge.mall.order.vo.AfterSaleVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/21
 */
@RestController
@RequestMapping("/afterSale")
public class AfterSaleController {
    @Resource
    private AfterSaleService afterSaleService;

    @PostMapping("/apply")
    public R<Long> apply(@Validated @RequestBody AfterSaleAddVo afterSaleAddVo, HttpServletRequest request) {
        return afterSaleService.apply(afterSaleAddVo, request);
    }

    @PostMapping("/pageList")
    public R<PageData<AfterSalePageResVo>> pageList(@RequestBody AfterSalePageReqVo afterSalePageReqVo) {
        return afterSaleService.pageList(afterSalePageReqVo);
    }

    @PostMapping("/{id}")
    public R<AfterSaleVo> getAfterSale(@PathVariable("id") Long id) {
        return afterSaleService.getAfterSale(id);
    }

    @PostMapping("/cancel/{id}")
    public R<Void> cancel(@PathVariable("id") Long id) {
        return afterSaleService.cancel(id);
    }
}
