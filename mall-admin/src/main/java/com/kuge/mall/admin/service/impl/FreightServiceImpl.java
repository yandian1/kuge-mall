package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.FreightService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonFreightEntity;
import com.kuge.mall.common.service.CommonFreightService;
import com.kuge.mall.common.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/21
 */
@Service("freightService")
public class FreightServiceImpl implements FreightService {

    @Resource
    private CommonFreightService commonFreightService;

    @Override
    public R<Void> add(FreightAddVo freightAddVo) {
        if (
            commonFreightService.exists(new QueryWrapper<CommonFreightEntity>().eq("name", freightAddVo.getName()))
        ) {
            return R.fail("模板名重复");
        }

        CommonFreightEntity commonFreightEntity = new CommonFreightEntity();
        BeanUtils.copyProperties(freightAddVo, commonFreightEntity);

        if (!commonFreightService.save(commonFreightEntity)) {
            return R.fail("添加模板失败");
        }

        return R.success("添加模板成功");
    }

    @Override
    public R<Void> updateFreight(FreightUpdateVo freightUpdateVo) {
        // 更新的模板不存在
        CommonFreightEntity commonFreightEntity = commonFreightService.getById(freightUpdateVo.getId());
        if (commonFreightEntity == null) {
            return R.fail("模板不存在");
        }

        if (
            commonFreightService.exists(
                new QueryWrapper<CommonFreightEntity>()
                    .ne("id", freightUpdateVo.getId())
                    .eq("name", freightUpdateVo.getName())
            )
        ) {
            return R.fail("模板名重复");
        }

        BeanUtils.copyProperties(freightUpdateVo, commonFreightEntity);

        if (!commonFreightService.updateById(commonFreightEntity)) {
            return R.fail("编辑模板失败");
        }

        return R.success("编辑模板成功");
    }

    @Override
    public R<Void> del(Long id) {
        // 模板不存在
        if (commonFreightService.getById(id) == null) {
            return R.fail("模板不存在");
        }
        
        // 删除模板
        if (!commonFreightService.removeById(id)) {
            return R.fail("删除模板失败");
        }

        return R.success("删除模板成功");
    }

    @Override
    public R<PageData<FreightPageItemVo>> pageList(FreightPageReqVo freightPageReqVo) {
        String name = freightPageReqVo.getName();
        String type = freightPageReqVo.getType();

        QueryWrapper<CommonFreightEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");

        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.hasLength(type)) {
            wrapper.eq("type", type);
        }

        Page<CommonFreightEntity> res = commonFreightService.page(new Page<>(freightPageReqVo.getPageNum(), freightPageReqVo.getPageSize()), wrapper);
        PageData<FreightPageItemVo> pageData = PageData.getPageData(res, FreightPageItemVo.class);
        pageData.getList().forEach(item -> {
            item.setTypeDesc(FreightUtils.getTypeDesc(item.getType()));
        });

        return R.success(pageData);
    }

    @Override
    public R<FreightDetailVo> getFreight(Long id) {
        CommonFreightEntity commonFreightEntity = commonFreightService.getById(id);
        if (commonFreightEntity == null) {
            throw new CustomException("运费模板不存在");
        }

        FreightDetailVo freightDetailVo = new FreightDetailVo();
        BeanUtils.copyProperties(commonFreightEntity, freightDetailVo);

        return R.success(freightDetailVo);
    }

    @Override
    public R<List<FreightItemVo>> list() {
        List<FreightItemVo> freightItemVos = new ArrayList<>();
        List<CommonFreightEntity> list = commonFreightService.list();
        if (CollectionUtils.isEmpty(list)) {
            return R.success(freightItemVos);
        }

        return R.success(
            list.stream().map(commonFreightEntity -> {
                FreightItemVo freightItemVo = new FreightItemVo();
                BeanUtils.copyProperties(commonFreightEntity, freightItemVo);
                return freightItemVo;
            }).collect(Collectors.toList())
        );
    }
}
