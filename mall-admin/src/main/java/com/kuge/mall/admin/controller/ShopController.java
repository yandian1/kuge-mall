package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.ShopService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 权限控制器
 * created by xbxie on 2024/5/13
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopService shopService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody ShopAddVo shopAddVo) {
        return shopService.add(shopAddVo);
    }

    @PostMapping("/update")
    public R<Void> updateShop(@Validated @RequestBody ShopUpdateVo shopUpdateVo) {
        return shopService.updateShop(shopUpdateVo);
    }

    @PostMapping("/{id}")
    public R<ShopDetailVo> getShop(@PathVariable("id") Long id) {
        return shopService.getShop(id);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return shopService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<ShopPageResVo>> pageList(@Validated @RequestBody ShopPageReqVo brandPageReqVo) {
        return shopService.pageList(brandPageReqVo);
    }

    @PostMapping("/list")
    public R<List<ShopVo>> list() {
        return shopService.list();
    }
}
