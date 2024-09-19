package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonDictTypeEntity;
import com.kuge.mall.common.entity.CommonDictValueEntity;
import com.kuge.mall.common.service.CommonDictTypeService;
import com.kuge.mall.common.service.CommonDictValueService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.kuge.mall.admin.service.DictValueService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-07-05 21:41:29
 */
@Service("dictNameService")
public class DictValueServiceImpl implements DictValueService {

    @Resource
    private CommonDictValueService commonDictValueService;

    @Resource
    private CommonDictTypeService commonDictTypeService;
    
    @Override
    public R<Void> add(DictValueAddVo dictValueAddVo) {
        Long pid = dictValueAddVo.getPid();

        // 字典类型不存在
        if (commonDictTypeService.getById(pid) == null) {
            return R.fail("字典类型不存在");
        }

        // 字典值重复
        List<CommonDictValueEntity> list = commonDictValueService.list(new QueryWrapper<CommonDictValueEntity>().eq("pid", pid).eq("value", dictValueAddVo.getValue()));
        if (!CollectionUtils.isEmpty(list)) {
            return R.fail("字典值重复");
        }

        CommonDictValueEntity commonDictValueEntity = new CommonDictValueEntity();
        BeanUtils.copyProperties(dictValueAddVo, commonDictValueEntity);

        if (!commonDictValueService.save(commonDictValueEntity)) {
            return R.fail("添加字典值失败");
        }

        return R.success("添加字典值成功");
    }

    @Override
    public R<Void> del(Long id) {
        // 字典值不存在
        if (commonDictValueService.getById(id) == null) {
            return R.fail("字典值不存在");
        }

        // 删除字典值
        if (!commonDictValueService.removeById(id)) {
            return R.fail("删除字典值失败");
        }

        return R.success("删除字典值成功");
    }

    @Override
    public R<Void> updateDict(DictValueUpdateVo dictValueUpdateVo) {
        Long pid = dictValueUpdateVo.getPid();

        // 字典类型不存在
        if (commonDictTypeService.getById(pid) == null) {
            return R.fail("字典类型不存在");
        }
        
        Long id = dictValueUpdateVo.getId();
        // 更新的字典值不存在
        CommonDictValueEntity targetCommonDictValueEntity = commonDictValueService.getById(id);
        if (targetCommonDictValueEntity == null) {
            return R.fail("字典值不存在");
        }

        // 字典值的类型不匹配
        if (!Objects.equals(targetCommonDictValueEntity.getPid(), pid)) {
            return R.fail("字典值的类型不匹配");
        }

        // 字典值重复
        if (!CollectionUtils.isEmpty(
            commonDictValueService.list(
                new QueryWrapper<CommonDictValueEntity>().eq("pid", pid).ne("id", id).eq("value", dictValueUpdateVo.getValue())
            )
        )) {
            return R.fail("字典值重复");
        }

        CommonDictValueEntity commonDictValueEntity = new CommonDictValueEntity();
        BeanUtils.copyProperties(dictValueUpdateVo, commonDictValueEntity);

        if (!commonDictValueService.updateById(commonDictValueEntity)) {
            return R.fail("更新字典值失败");
        }


        return R.success("更新字典值成功");
    }

    @Override
    public R<PageData<DictValuePageResVo>> pageList(DictValuePageReqVo dictValuePageReqVo) {
        Long pid = dictValuePageReqVo.getPid();
        String value = dictValuePageReqVo.getValue();

        QueryWrapper<CommonDictValueEntity> wrapper = new QueryWrapper<CommonDictValueEntity>().eq("pid", pid);
        if (StringUtils.hasLength(value)) {
            wrapper.like("value", value);
        }

        Page<CommonDictValueEntity> res = commonDictValueService.page(new Page<>(dictValuePageReqVo.getPageNum(), dictValuePageReqVo.getPageSize()), wrapper);

        PageData<DictValuePageResVo> pageData = PageData.getPageData(res, DictValuePageResVo.class);

        return R.success(pageData);
    }

    @Override
    public R<DictValueDetailVo> getDictValue(Long id) {
        CommonDictValueEntity commonDictValueEntity = commonDictValueService.getById(id);
        if (commonDictValueEntity == null) {
            throw new CustomException("字典值不存在");
        }

        DictValueDetailVo dictValueDetailVo = new DictValueDetailVo();
        BeanUtils.copyProperties(commonDictValueEntity, dictValueDetailVo);

        return R.success(dictValueDetailVo);
    }

    @Override
    public R<List<DictValueListItemVo>> list(String code) {
        List<CommonDictTypeEntity> commonDictTypeEntities = commonDictTypeService.list(new QueryWrapper<CommonDictTypeEntity>().eq("code", code));
        // 字典类型不存在
        if (CollectionUtils.isEmpty(commonDictTypeEntities)) {
            throw new CustomException("字典类型不存在");
        }

        List<CommonDictValueEntity> commonDictValueEntities = commonDictValueService.list(new QueryWrapper<CommonDictValueEntity>().eq("pid", commonDictTypeEntities.get(0).getId()));
        if (CollectionUtils.isEmpty(commonDictValueEntities)) {
            return R.success(new ArrayList<DictValueListItemVo>());
        }

        List<DictValueListItemVo> dictValueListItemVos = commonDictValueEntities.stream().map(commonDictValueEntity -> {
            DictValueListItemVo dictValueListItemVo = new DictValueListItemVo();
            BeanUtils.copyProperties(commonDictValueEntity, dictValueListItemVo);
            return dictValueListItemVo;
        }).collect(Collectors.toList());

        return R.success(dictValueListItemVos);
    }
}