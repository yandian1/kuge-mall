package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.kuge.mall.admin.service.SpuService;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Resource
    private SpuService spuService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody SpuAddVo spuAddVo) {
        return spuService.add(spuAddVo);
    }

    @PostMapping("/{id}")
    public R<SpuDetailVo> getSpu(@PathVariable("id") Long id) {
        return spuService.getSpu(id);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody SpuUpdateVo spuUpdateVo) {
        return spuService.updateSpu(spuUpdateVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return spuService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<SpuPageResVo>> pageList(@RequestBody SpuPageReqVo spuPageVo) {
        return spuService.pageList(spuPageVo);
    }

    @PostMapping("/changeStatus")
    public R<Void> changeStatus(@Validated @RequestBody UpdateSpuStatusReqVo updateSpuStatusReqVo) {
        return spuService.changeStatus(updateSpuStatusReqVo);
    }
}
