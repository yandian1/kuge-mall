package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.kuge.mall.admin.service.DictTypeService;

import java.util.List;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
@RestController
@RequestMapping("/dict/type")
public class DictTypeController {
    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody DictTypeAddVo dictTypeAddVo) {
        return dictTypeService.add(dictTypeAddVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return dictTypeService.del(id);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody DictTypeUpdateVo dictTypeUpdateVo) {
        return dictTypeService.updateDictType(dictTypeUpdateVo);
    }

    @PostMapping("/pageList")
    public R<PageData<DictTypePageResVo>> pageList(@Validated @RequestBody DictTypePageReqVo dictTypePageReqVo) {
        return dictTypeService.pageList(dictTypePageReqVo);
    }

    @PostMapping("/{id}")
    public R<DictTypeDetailVo> getDictType(@PathVariable("id") Long id) {
        return dictTypeService.getDictType(id);
    }
}
