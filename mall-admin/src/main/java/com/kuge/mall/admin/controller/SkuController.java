package com.kuge.mall.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import com.kuge.mall.admin.service.SkuService;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@RestController
@RequestMapping("/sku")
public class SkuController {
    @Resource
    private SkuService skuService;
}
