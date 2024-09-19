package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonBrandEntity;
import com.kuge.mall.common.service.CommonBrandService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.kuge.mall.admin.service.BrandService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {
    @Resource
    private CommonBrandService commonBrandService;
    
    @Override
    public R<PageData<BrandPageResVo>> pageList(BrandPageReqVo brandPageReqVo) {
        String name = brandPageReqVo.getName();

        QueryWrapper<CommonBrandEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }

        Page<CommonBrandEntity> page = commonBrandService.page(new Page<>(brandPageReqVo.getPageNum(), brandPageReqVo.getPageSize()), wrapper);
        PageData<BrandPageResVo> pageData = PageData.getPageData(page, BrandPageResVo.class);

        return R.success(pageData);
    }

    @Override
    public R<Void> add(BrandAddVo brandAddVo) {
        String name = brandAddVo.getName();

        // 判断品牌名重复
        if (commonBrandService.exists(new QueryWrapper<CommonBrandEntity>().eq("name", name))) {
            return R.fail("品牌名重复");
        }

        // 插入品牌
        CommonBrandEntity CommonBrandEntity = new CommonBrandEntity();
        BeanUtils.copyProperties(brandAddVo, CommonBrandEntity);

        if (!commonBrandService.save(CommonBrandEntity)) {
            return R.fail("添加品牌失败");
        }

        return R.success("添加品牌成功");
    }

    @Override
    public R<Void> updateBrand(BrandUpdateVo brandUpdateVo) {
        // 更新的品牌不存在
        Long id = brandUpdateVo.getId();
        if (commonBrandService.getById(id) == null) {
            return R.fail("品牌不存在");
        }

        if (
            commonBrandService.exists(
                new QueryWrapper<CommonBrandEntity>()
                    .ne("id", id)
                    .and(i -> i.eq("name", brandUpdateVo.getName()))
            )
        ) {
            return R.fail("品牌名重复");
        }

        CommonBrandEntity CommonBrandEntity = new CommonBrandEntity();
        BeanUtils.copyProperties(brandUpdateVo, CommonBrandEntity);

        if (!commonBrandService.updateById(CommonBrandEntity)) {
            return R.fail("更新品牌失败");
        }

        return R.success("更新品牌成功");
    }

    @Override
    public R<BrandDetailVo> getBrand(Long id) {
        CommonBrandEntity CommonBrandEntity = commonBrandService.getById(id);
        if (CommonBrandEntity == null) {
            throw new CustomException("品牌不存在");
        }

        BrandDetailVo brandDetailVo = new BrandDetailVo();
        BeanUtils.copyProperties(CommonBrandEntity, brandDetailVo);
        return R.success(brandDetailVo);
    }

    @Override
    public R<Void> del(Long id) {
        // 品牌不存在
        if (commonBrandService.getById(id) == null) {
            return R.fail("品牌不存在");
        }

        if (!commonBrandService.removeById(id)) {
            return R.fail("删除品牌失败");
        }

        return R.success("删除品牌成功");
    }

    @Override
    public R<List<BrandDetailVo>> getList() {
        List<CommonBrandEntity> list = commonBrandService.list();
        if (CollectionUtils.isEmpty(list)) {
            return R.success(new ArrayList<>());
        }

        return R.success(list.stream().map(item -> {
            BrandDetailVo brandDetailVo = new BrandDetailVo();
            BeanUtils.copyProperties(item, brandDetailVo);
            return brandDetailVo;
        }).collect(Collectors.toList()));
    }
}