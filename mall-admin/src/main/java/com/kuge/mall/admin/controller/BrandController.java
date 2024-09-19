package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.kuge.mall.admin.service.BrandService;
import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Resource
    private BrandService brandService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody BrandAddVo brandAddVo) {
        return brandService.add(brandAddVo);
    }

    @PostMapping("/update")
    public R<Void> updateBrand(@Validated @RequestBody BrandUpdateVo brandUpdateVo) {
        return brandService.updateBrand(brandUpdateVo);
    }

    @PostMapping("/{id}")
    public R<BrandDetailVo> getBrand(@PathVariable("id") Long id) {
        return brandService.getBrand(id);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return brandService.del(id);
    }

    @PostMapping("/list")
    public R<List<BrandDetailVo>> getList() {
        return brandService.getList();
    }

    @PostMapping("/pageList")
    public R<PageData<BrandPageResVo>> pageList(@Validated @RequestBody BrandPageReqVo brandPageReqVo) {
        return brandService.pageList(brandPageReqVo);
    }
}
