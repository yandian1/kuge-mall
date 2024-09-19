package com.kuge.mall.product.controller;

import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.SpuService;
import com.kuge.mall.product.vo.SpuDetailVo;
import com.kuge.mall.product.vo.SpuPageReqVo;
import com.kuge.mall.product.vo.SpuPageResVo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Resource
    private SpuService spuService;

    @PostMapping("/pageList")
    public R<PageData<SpuPageResVo>> pageList(@RequestBody SpuPageReqVo spuPageVo, HttpServletRequest request) {
        return spuService.pageList(spuPageVo, request);
    }

    @PostMapping("/{id}")
    public R<SpuDetailVo> getSpu(@PathVariable("id") Long id, HttpServletRequest request) {
        return spuService.getSpu(id, request);
    }
}
