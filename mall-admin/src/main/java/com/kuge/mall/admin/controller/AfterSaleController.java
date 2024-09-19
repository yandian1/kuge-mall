package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.AfterSaleService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
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

    @PostMapping("/pageList")
    public R<PageData<AfterSalePageResVo>> pageList(@RequestBody AfterSalePageReqVo afterSalePageReqVo) {
        return afterSaleService.pageList(afterSalePageReqVo);
    }

    @PostMapping("/{id}")
    public R<AfterSaleVo> getAfterSale(@PathVariable("id") Long id) {
        return afterSaleService.getAfterSale(id);
    }

    @PostMapping("/update")
    public R<Void> updateAfterSale(@Validated @RequestBody AfterSaleUpdateVo reqVo) {
        return afterSaleService.updateAfterSale(reqVo);
    }

    @PostMapping("/notify")
    public void notify(HttpServletRequest request) {
        System.out.println("parseRefundNotify");
        afterSaleService.notify(request);
    }
}
