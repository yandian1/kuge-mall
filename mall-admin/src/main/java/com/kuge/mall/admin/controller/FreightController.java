package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.FreightService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 * created by xbxie on 2024/4/24
 */
@RestController
@RequestMapping("/freight")
public class FreightController {

    @Resource
    private FreightService freightService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody FreightAddVo freightAddVo) {
        return freightService.add(freightAddVo);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody FreightUpdateVo freightUpdateVo) {
        return freightService.updateFreight(freightUpdateVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return freightService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<FreightPageItemVo>> pageList(@Validated @RequestBody FreightPageReqVo freightPageReqVo) {
        return freightService.pageList(freightPageReqVo);
    }

    @PostMapping("/list")
    public R<List<FreightItemVo>> list() {
        return freightService.list();
    }

    @PostMapping("/{id}")
    public R<FreightDetailVo> getFreight(@PathVariable("id") Long id) {
        return freightService.getFreight(id);
    }
}
