package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.service.ActivityService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024-08-08 04:34:38
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody ActivityAddVo activityAddVo) {
        return activityService.add(activityAddVo);
    }

    @PostMapping("/update")
    public R<Void> updateActivity(@Validated @RequestBody ActivityUpdateVo activityUpdateVo) {
        return activityService.updateActivity(activityUpdateVo);
    }

    @PostMapping("/del/{id}")
    public R<Void> del(@PathVariable("id") Long id) {
        return activityService.del(id);
    }

    @PostMapping("/pageList")
    public R<PageData<ActivitylPageItemVo>> pageList(@Validated @RequestBody ActivityPageReqVo activityPageReqVo) {
        return activityService.pageList(activityPageReqVo);
    }

    @PostMapping("/{id}")
    public R<ActivitylDetailVo> getActivity(@PathVariable("id") Long id) {
        return activityService.getActivity(id);
    }
}
