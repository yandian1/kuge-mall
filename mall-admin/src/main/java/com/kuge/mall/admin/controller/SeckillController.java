package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.SeckillService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody SeckillAddVo seckillAddVo) {
        return seckillService.add(seckillAddVo);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody SeckillUpdateVo seckillUpdateVo) {
        return seckillService.updateSeckill(seckillUpdateVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return seckillService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<SeckillPageItemVo>> pageList(@Validated @RequestBody SeckillPageReqVo seckillPageReqVo) {
        return seckillService.pageList(seckillPageReqVo);
    }

    @PostMapping("/{id}")
    public R<SeckillDetailVo> getSeckill(@PathVariable("id") Long id) {
        return seckillService.getSeckill(id);
    }
}
