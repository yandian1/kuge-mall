package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.kuge.mall.admin.service.DictValueService;

import java.util.List;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
@RestController
@RequestMapping("/dict/value")
public class DictValueController {
    @Resource
    private DictValueService dictValueService;
    
    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody DictValueAddVo dictValueAddVo) {
        return dictValueService.add(dictValueAddVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return dictValueService.del(id);
    }

    @PostMapping("/update")
    public R<Void> update(@Validated @RequestBody DictValueUpdateVo dictValueUpdateVo) {
        return dictValueService.updateDict(dictValueUpdateVo);
    }

    @PostMapping("/pageList")
    public R<PageData<DictValuePageResVo>> pageList(@Validated @RequestBody DictValuePageReqVo dictValuePageReqVo) {
        return dictValueService.pageList(dictValuePageReqVo);
    }

    @PostMapping("/list/{code}")
    public R<List<DictValueListItemVo>> list(@PathVariable("code") String code) {
        return dictValueService.list(code);
    }

    @PostMapping("/{id}")
    public R<DictValueDetailVo> getDictValue(@PathVariable("id") Long id) {
        return dictValueService.getDictValue(id);
    }
}
