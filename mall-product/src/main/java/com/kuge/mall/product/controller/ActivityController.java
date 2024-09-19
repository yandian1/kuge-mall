package com.kuge.mall.product.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.ActivityService;
import com.kuge.mall.product.vo.ActivitylDetailVo;
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

    @PostMapping("/{id}")
    public R<ActivitylDetailVo> getActivity(@PathVariable("id") Long id) {
        return activityService.getActivity(id);
    }
}
