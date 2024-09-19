package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.ActivityService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.CommonActivitySectionService;
import com.kuge.mall.common.service.CommonActivitySectionSpuService;
import com.kuge.mall.common.service.CommonActivityService;
import com.kuge.mall.common.service.CommonSpuService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.GoodsUtils;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/21
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private CommonActivityService commonActivityService;

    @Resource
    private CommonActivitySectionService commonActivitySectionService;
    
    @Resource
    private CommonActivitySectionSpuService commonActivitySectionSpuService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<Void> add(ActivityAddVo activityAddVo) {
        List<ActivityAddVo.Section> sections = activityAddVo.getSections();
        if (CollectionUtils.isEmpty(sections)) {
            return R.fail("请添加活动分区");
        }

        for (ActivityAddVo.Section section : sections) {
            List<Long> spuIds = section.getSpuIds();
            if (CollectionUtils.isEmpty(spuIds)) {
                return R.fail("请添加活动分区商品");
            }
        }

        CommonActivityEntity commonActivityEntity = new CommonActivityEntity();
        BeanUtils.copyProperties(activityAddVo, commonActivityEntity);
        if (!commonActivityService.save(commonActivityEntity)) {
            return R.fail("添加活动失败");
        }

        for (ActivityAddVo.Section section : sections) {
            CommonActivitySectionEntity commonActivitySectionEntity = new CommonActivitySectionEntity();
            commonActivitySectionEntity.setActivityId(commonActivityEntity.getId());
            commonActivitySectionEntity.setTitle(section.getTitle());
            if (!commonActivitySectionService.save(commonActivitySectionEntity)) {
                throw new CustomException("添加活动失败");
            }

            // 时间段商品
            List<CommonActivitySectionSpuEntity> commonActivitySectionSpuEntities = section.getSpuIds().stream().map(spuId -> {
                CommonActivitySectionSpuEntity commonActivitySectionSpuEntity = new CommonActivitySectionSpuEntity();
                commonActivitySectionSpuEntity.setActivitySectionId(commonActivitySectionEntity.getId());
                commonActivitySectionSpuEntity.setSpuId(spuId);
                return commonActivitySectionSpuEntity;
            }).collect(Collectors.toList());

            if (!commonActivitySectionSpuService.saveBatch(commonActivitySectionSpuEntities)) {
                throw new CustomException("添加秒动失败");
            }
        }

        return R.success("添加活动成功");
    }

    @Override
    public R<Void> updateActivity(ActivityUpdateVo activityUpdateVo) {
        // 更新的活动不存在
        CommonActivityEntity commonActivityEntity = commonActivityService.getById(activityUpdateVo.getId());
        if (commonActivityEntity == null) {
            return R.fail("活动不存在");
        }

        List<ActivityAddVo.Section> sections = activityUpdateVo.getSections();
        if (CollectionUtils.isEmpty(sections)) {
            return R.fail("请添加活动分区");
        }
        for (ActivityAddVo.Section section : sections) {
            List<Long> spuIds = section.getSpuIds();
            if (CollectionUtils.isEmpty(spuIds)) {
                return R.fail("请添加活动分区商品");
            }
        }

        BeanUtils.copyProperties(activityUpdateVo, commonActivityEntity);
        if (!commonActivityService.updateById(commonActivityEntity)) {
            return R.fail("编辑活动失败");
        }

        // 先删除
        List<CommonActivitySectionEntity> commonActivitySectionEntities = commonActivitySectionService.list(
            new QueryWrapper<CommonActivitySectionEntity>().eq("activity_id", commonActivityEntity.getId())
        );

        if (!CollectionUtils.isEmpty(commonActivitySectionEntities)) {
            List<Long> activitySectionIds = commonActivitySectionEntities.stream().map(CommonActivitySectionEntity::getId).collect(Collectors.toList());
            commonActivitySectionService.removeByIds(activitySectionIds);
            commonActivitySectionSpuService.remove(new QueryWrapper<CommonActivitySectionSpuEntity>().in("activity_section_id", activitySectionIds));
        }

        // 后添加
        for (ActivityAddVo.Section section : sections) {
            CommonActivitySectionEntity commonActivitySectionEntity = new CommonActivitySectionEntity();
            commonActivitySectionEntity.setActivityId(commonActivityEntity.getId());
            commonActivitySectionEntity.setTitle(section.getTitle());
            if (!commonActivitySectionService.save(commonActivitySectionEntity)) {
                throw new CustomException("编辑活动失败");
            }

            // 时间段商品
            List<CommonActivitySectionSpuEntity> commonActivitySectionSpuEntities = section.getSpuIds().stream().map(spuId -> {
                CommonActivitySectionSpuEntity commonActivitySectionSpuEntity = new CommonActivitySectionSpuEntity();
                commonActivitySectionSpuEntity.setActivitySectionId(commonActivitySectionEntity.getId());
                commonActivitySectionSpuEntity.setSpuId(spuId);
                return commonActivitySectionSpuEntity;
            }).collect(Collectors.toList());

            if (!commonActivitySectionSpuService.saveBatch(commonActivitySectionSpuEntities)) {
                throw new CustomException("编辑秒动失败");
            }
        }

        stringRedisTemplate.delete(RedisKeyConstant.ACTIVITY_PREFIX + activityUpdateVo.getId());

        return R.success();
    }

    @Override
    public R<Void> del(Long id) {
        // 删除的活动不存在
        CommonActivityEntity commonActivityEntity = commonActivityService.getById(id);
        if (commonActivityEntity == null) {
            return R.fail("删除的活动不存在");
        }

        // 删除活动
        if (!commonActivityService.removeById(id)) {
            return R.fail("删除活动失败");
        }

        List<CommonActivitySectionEntity> commonActivitySectionEntities = commonActivitySectionService.list(
            new QueryWrapper<CommonActivitySectionEntity>().eq("activity_id", id)
        );

        if (!CollectionUtils.isEmpty(commonActivitySectionEntities)) {
            List<Long> activitySectionIds = commonActivitySectionEntities.stream().map(CommonActivitySectionEntity::getId).collect(Collectors.toList());
            if (!commonActivitySectionService.removeByIds(activitySectionIds)) {
                return R.fail("删除活动失败");
            }
            if (!commonActivitySectionSpuService.remove(new QueryWrapper<CommonActivitySectionSpuEntity>().in("activity_section_id", activitySectionIds))) {
                return R.fail("删除活动失败");
            }
        }

        return R.success();
    }

    @Override
    public R<PageData<ActivitylPageItemVo>> pageList(ActivityPageReqVo activityPageReqVo) {
        String name = activityPageReqVo.getName();

        QueryWrapper<CommonActivityEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }

        return R.success(
            PageData.getPageData(
                commonActivityService.page(new Page<>(activityPageReqVo.getPageNum(), activityPageReqVo.getPageSize()), wrapper),
                ActivitylPageItemVo.class
            )
        );
    }

    @Override
    public R<ActivitylDetailVo> getActivity(Long id) {
        CommonActivityEntity commonActivityEntity = commonActivityService.getById(id);
        if (commonActivityEntity == null) {
            throw new CustomException("活动不存在");
        }

        ActivitylDetailVo activitylDetailVo = new ActivitylDetailVo();
        BeanUtils.copyProperties(commonActivityEntity, activitylDetailVo);

        List<CommonActivitySectionEntity> commonActivitySectionEntities = commonActivitySectionService.list(
            new QueryWrapper<CommonActivitySectionEntity>().eq("activity_id", id)
        );

        if (!CollectionUtils.isEmpty(commonActivitySectionEntities)) {
            activitylDetailVo.setSections(
                commonActivitySectionEntities.stream().map(commonActivitySectionEntity -> {
                    ActivitylDetailVo.Section section = new ActivitylDetailVo.Section();
                    section.setTitle(commonActivitySectionEntity.getTitle());

                    List<CommonActivitySectionSpuEntity> commonActivitySectionSpuEntities = commonActivitySectionSpuService.list(
                        new QueryWrapper<CommonActivitySectionSpuEntity>().eq("activity_section_id", commonActivitySectionEntity.getId())
                    );
                    if (!CollectionUtils.isEmpty(commonActivitySectionSpuEntities)) {
                        List<ActivitylDetailVo.GoodsItem> goodList = commonActivitySectionSpuEntities.stream().map(commonActivitySectionSpuEntity -> {
                            ActivitylDetailVo.GoodsItem goodsItem = new ActivitylDetailVo.GoodsItem();
                            CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonActivitySectionSpuEntity.getSpuId());
                            if (commonSpuEntity == null || commonSpuEntity.getStatus() == null || commonSpuEntity.getStatus() != 1) {
                                return null;
                            }
                            BeanUtils.copyProperties(commonSpuEntity, goodsItem);
                            goodsItem.setStatusDesc(GoodsUtils.getDesc(commonSpuEntity.getStatus()));
                            return goodsItem;
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                        section.setGoodsList(goodList);
                    }
                    return section;
                }).collect(Collectors.toList())
            );
        }

        return R.success(activitylDetailVo);
    }
}
