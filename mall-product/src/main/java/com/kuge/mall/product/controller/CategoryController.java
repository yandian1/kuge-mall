package com.kuge.mall.product.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.CategoryService;
import com.kuge.mall.product.vo.CategoryListReqVo;
import com.kuge.mall.product.vo.CategoryVo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;


    @PostMapping("/list")
    public R<List<CategoryVo>> list(@RequestBody CategoryListReqVo categoryListReqVo) {
        return categoryService.list(categoryListReqVo);
    }
}
