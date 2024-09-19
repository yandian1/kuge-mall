package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonCategoryEntity;
import com.kuge.mall.common.service.CommonCategoryService;
import com.kuge.mall.common.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.kuge.mall.admin.service.CategoryService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CommonCategoryService commonCategoryService;
    
    @Override
    public R<PageData<CategoryPageResVo>> pageList(CategoryPageReqVo categoryPageReqVo) {
        String name = categoryPageReqVo.getName();

        if (StringUtils.hasLength(name)) {
            QueryWrapper<CommonCategoryEntity> wrapper = new QueryWrapper<CommonCategoryEntity>().like("name", name);

            wrapper.orderByAsc("sort");

            Page<CommonCategoryEntity> page = commonCategoryService.page(new Page<>(categoryPageReqVo.getPageNum(), categoryPageReqVo.getPageSize()), wrapper);
            PageData<CategoryPageResVo> pageData = PageData.getPageData(page, CategoryPageResVo.class);

            return R.success(pageData);
        } else {
            List<CategoryPageResVo> categoryPageResVos = TreeUtils.genTree(null, commonCategoryService.list(), CategoryPageResVo.class, true);
            PageData<CategoryPageResVo> pageData = PageData.getPageData(categoryPageReqVo.getPageNum(), categoryPageReqVo.getPageSize(), categoryPageResVos);

            return R.success(pageData);
        }
    }

    @Override
    public R<Long> add(CategoryAddVo categoryAddVo) {
        String name = categoryAddVo.getName();

        // 判断分类名是否已存在
        if (
            commonCategoryService.exists(
                new QueryWrapper<CommonCategoryEntity>()
                    .eq("name", name)
                    .eq("pid", categoryAddVo.getPid())
            )
        ) {
            throw new CustomException("分类名重复");
        }

        // 插入分类
        CommonCategoryEntity commonCategoryEntity = new CommonCategoryEntity();
        BeanUtils.copyProperties(categoryAddVo, commonCategoryEntity);

        if (!commonCategoryService.save(commonCategoryEntity)) {
            throw new CustomException("添加分类失败");
        }

        return R.success(commonCategoryEntity.getId());
    }

    @Override
    public R<List<CategoryTreeVo>> tree() {
        List<CommonCategoryEntity> categoryEntities = commonCategoryService.list();
        if (CollectionUtils.isEmpty(categoryEntities)) {
            return R.success(new ArrayList<>());
        }

        List<CategoryTreeVo> categoryTreeVos = TreeUtils.genTree(null, commonCategoryService.list(), CategoryTreeVo.class, false);

        return R.success(categoryTreeVos);
    }

    @Override
    public R<Void> del(Long id) {
        // 分类不存在
        if (commonCategoryService.getById(id) == null) {
            return R.fail("分类不存在");
        }

        // 删除自身及子分类
        List<Object> delIds = new ArrayList<>(Arrays.asList(id));
        delIds.addAll(TreeUtils.genCids(id, commonCategoryService.list()));
        if (!commonCategoryService.removeByIds(delIds)) {
            return R.fail("删除分类失败");
        }

        return R.success("删除分类成功");
    }

    @Override
    public R<CategoryDetailVo> getCategory(Long id) {
        CommonCategoryEntity CommonCategoryEntity = commonCategoryService.getById(id);
        if (CommonCategoryEntity == null) {
            throw new CustomException("分类不存在");
        }

        CategoryDetailVo categoryDetailVo = new CategoryDetailVo();
        BeanUtils.copyProperties(CommonCategoryEntity, categoryDetailVo);

        List<Long> pids = TreeUtils.genPids(CommonCategoryEntity.getId(), commonCategoryService.list());
        categoryDetailVo.setPids(pids);

        return R.success(categoryDetailVo);
    }

    @Override
    public R<Void> updateCategory(CategoryUpdateVo categoryUpdateVo) {
        // 更新的分类不存在
        Long id = categoryUpdateVo.getId();
        if (commonCategoryService.getById(id) == null) {
            return R.fail("分类不存在");
        }

        List<CommonCategoryEntity> categoryEntities = commonCategoryService.list();
        Long pid = categoryUpdateVo.getPid();

        // 禁止将自身或后代设置为父分类，否则会形成闭环
        if (pid != null) {
            List<Long> childrenIds = TreeUtils.genCids(categoryUpdateVo.getId(), categoryEntities);
            if (childrenIds.contains(pid) || Objects.equals(id, pid)) {
                return R.fail("无法将自身或后代设置为父分类");
            }
        }

        // 分类名重复
        if (
            categoryEntities.stream().anyMatch(CommonCategoryEntity -> !Objects.equals(CommonCategoryEntity.getId(), id) && Objects.equals(CommonCategoryEntity.getName(), categoryUpdateVo.getName()))
        ) {
            return R.fail("分类名重复");
        }

        CommonCategoryEntity commonCategoryEntity = new CommonCategoryEntity();
        BeanUtils.copyProperties(categoryUpdateVo, commonCategoryEntity);
        System.out.println("categoryUpdateVo = " + categoryUpdateVo);
        System.out.println("commonCategoryEntity = " + commonCategoryEntity);

        if (!commonCategoryService.updateById(commonCategoryEntity)) {
            return R.fail("更新分类失败");
        }

        return R.success("更新分类成功");
    }
}