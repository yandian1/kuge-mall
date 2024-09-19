package com.kuge.mall.product.service.impl;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.entity.CommonCategoryEntity;
import com.kuge.mall.common.service.CommonCategoryService;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.CategoryService;
import com.kuge.mall.product.vo.CategoryListReqVo;
import com.kuge.mall.product.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CommonCategoryService commonCategoryService;

    @Override
    public R<List<CategoryVo>> list(CategoryListReqVo categoryListReqVo) {
        Long categoryId = categoryListReqVo.getId();

        QueryWrapper<CommonCategoryEntity> query;

        if (categoryId == null) {
            query = new QueryWrapper<CommonCategoryEntity>().isNull("pid");
        } else {
            query = new QueryWrapper<CommonCategoryEntity>().eq("pid", categoryListReqVo.getId());
        }

        List<CommonCategoryEntity> commonCategoryEntities = commonCategoryService.list(query);
        List<CategoryVo> categoryVos = commonCategoryEntities.stream().map(commonCategoryEntity -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(commonCategoryEntity, categoryVo);
            return categoryVo;
        }).collect(Collectors.toList());

        return R.success(categoryVos);
    }
}