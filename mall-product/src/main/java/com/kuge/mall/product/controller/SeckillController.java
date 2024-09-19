package com.kuge.mall.product.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.SeckillService;
import com.kuge.mall.product.vo.SeckillDetailVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/7/16
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    @PostMapping("/{id}")
    public R<SeckillDetailVo> getSeckill(@PathVariable("id") Long id) {
        return seckillService.getSeckill(id);
    }
}
