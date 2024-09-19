package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.ShopService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("shopService")
public class ShopServiceImpl implements ShopService {
    @Resource
    private CommonShopService commonShopService;

    @Override
    public R<List<ShopVo>> list() {
        List<CommonShopEntity> list = commonShopService.list();
        if (CollectionUtils.isEmpty(list)) {
            return R.success(new ArrayList<>());
        }

        return R.success(list.stream().map(item -> {
            ShopVo shopVo = new ShopVo();
            BeanUtils.copyProperties(item, shopVo);
            return shopVo;
        }).collect(Collectors.toList()));
    }

    @Override
    public R<Void> add(ShopAddVo shopAddVo) {
        String name = shopAddVo.getName();

        // 判断店铺名重复
        if (commonShopService.exists(new QueryWrapper<CommonShopEntity>().eq("name", name))) {
            return R.fail("店铺名重复");
        }

        // 插入店铺
        CommonShopEntity CommonShopEntity = new CommonShopEntity();
        BeanUtils.copyProperties(shopAddVo, CommonShopEntity);

        if (!commonShopService.save(CommonShopEntity)) {
            return R.fail("添加店铺失败");
        }

        return R.success("添加店铺成功");
    }

    @Override
    public R<Void> updateShop(ShopUpdateVo shopUpdateVo) {
        // 更新的店铺不存在
        Long id = shopUpdateVo.getId();
        if (commonShopService.getById(id) == null) {
            return R.fail("店铺不存在");
        }

        if (
            commonShopService.exists(
                new QueryWrapper<CommonShopEntity>()
                    .ne("id", id)
                    .and(i -> i.eq("name", shopUpdateVo.getName()))
            )
        ) {
            return R.fail("店铺名重复");
        }

        CommonShopEntity CommonShopEntity = new CommonShopEntity();
        BeanUtils.copyProperties(shopUpdateVo, CommonShopEntity);

        if (!commonShopService.updateById(CommonShopEntity)) {
            return R.fail("更新店铺失败");
        }

        return R.success("更新店铺成功");
    }

    @Override
    public R<ShopDetailVo> getShop(Long id) {
        CommonShopEntity commonShopEntity = commonShopService.getById(id);
        if (commonShopEntity == null) {
            throw new CustomException("店铺不存在");
        }

        ShopDetailVo shopDetailVo = new ShopDetailVo();
        BeanUtils.copyProperties(commonShopEntity, shopDetailVo);
        return R.success(shopDetailVo);
    }

    @Override
    public R<Void> del(Long id) {
        // 店铺不存在
        if (commonShopService.getById(id) == null) {
            return R.fail("店铺不存在");
        }

        if (!commonShopService.removeById(id)) {
            return R.fail("删除店铺失败");
        }

        return R.success("删除店铺成功");
    }

    @Override
    public R<PageData<ShopPageResVo>> pageList(ShopPageReqVo shopPageReqVo) {
        String name = shopPageReqVo.getName();

        QueryWrapper<CommonShopEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }

        Page<CommonShopEntity> page = commonShopService.page(new Page<>(shopPageReqVo.getPageNum(), shopPageReqVo.getPageSize()), wrapper);

        PageData<ShopPageResVo> pageData = PageData.getPageData(page, ShopPageResVo.class);

        return R.success(pageData);
    }
}