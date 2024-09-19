package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonDictTypeEntity;
import com.kuge.mall.common.service.CommonDictTypeService;
import com.kuge.mall.common.service.impl.CommonDictValueServiceImpl;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kuge.mall.admin.service.DictTypeService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
@Service("dictTypeService")
public class DictTypeServiceImpl implements DictTypeService {
    @Resource
    private CommonDictTypeService commonDictTypeService;
    @Autowired
    private CommonDictValueServiceImpl commonDictValueService;

    @Override
    public R<Void> add(DictTypeAddVo dictTypeAddVo) {
        List<CommonDictTypeEntity> list = commonDictTypeService.list();
        
        // 字典类型或类型code重复
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getType(), dictTypeAddVo.getType()))) {
                return R.fail("字典类型重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getCode(), dictTypeAddVo.getCode()))) {
                return R.fail("类型code重复");
            }
        }

        CommonDictTypeEntity commonDictTypeEntity = new CommonDictTypeEntity();
        BeanUtils.copyProperties(dictTypeAddVo, commonDictTypeEntity);

        if (!commonDictTypeService.save(commonDictTypeEntity)) {
            return R.fail("添加字典类型失败");
        }

        return R.success("添加字典类型成功");
    }

    @Override
    public R<Void> del(Long id) {
        // 名典类型不存在
        if (commonDictTypeService.getById(id) == null) {
            return R.fail("名典类型不存在");
        }

        // 删除名典类型
        if (!commonDictTypeService.removeById(id)) {
            return R.fail("删除名典类型失败");
        }

        return R.success("删除名典类型成功");
    }

    @Override
    public R<Void> updateDictType(DictTypeUpdateVo dictTypeUpdateVo) {
        Long id = dictTypeUpdateVo.getId();

        // 更新的字典类型不存在
        if (commonDictTypeService.getById(id) == null) {
            return R.fail("字典类型不存在");
        }

        // 字典类型或类型code重复
        List<CommonDictTypeEntity> list = commonDictTypeService.list(
            new QueryWrapper<CommonDictTypeEntity>().ne("id", id)
        );
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getType(), dictTypeUpdateVo.getType()))) {
                return R.fail("字典类型重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getCode(), dictTypeUpdateVo.getCode()))) {
                return R.fail("类型code重复");
            }
        }

        CommonDictTypeEntity commonDictTypeEntity = new CommonDictTypeEntity();
        BeanUtils.copyProperties(dictTypeUpdateVo, commonDictTypeEntity);

        if (!commonDictTypeService.updateById(commonDictTypeEntity)) {
            return R.fail("更新字典类型失败");
        }


        return R.success("更新字典类型成功");
    }

    @Override
    public R<PageData<DictTypePageResVo>> pageList(DictTypePageReqVo dictTypePageReqVo) {
        String type = dictTypePageReqVo.getType();

        QueryWrapper<CommonDictTypeEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(type)) {
            wrapper.like("type", type);
        }

        Page<CommonDictTypeEntity> res = commonDictTypeService.page(new Page<>(dictTypePageReqVo.getPageNum(), dictTypePageReqVo.getPageSize()), wrapper);

        PageData<DictTypePageResVo> pageData = PageData.getPageData(res, DictTypePageResVo.class);

        return R.success(pageData);
    }

    @Override
    public R<DictTypeDetailVo> getDictType(Long id) {
        CommonDictTypeEntity commonDictTypeEntity = commonDictTypeService.getById(id);
        if (commonDictTypeEntity == null) {
            throw new CustomException("字典类型不存在");
        }

        DictTypeDetailVo dictTypeDetailVo = new DictTypeDetailVo();
        BeanUtils.copyProperties(commonDictTypeEntity, dictTypeDetailVo);

        return R.success(dictTypeDetailVo);
    }
}