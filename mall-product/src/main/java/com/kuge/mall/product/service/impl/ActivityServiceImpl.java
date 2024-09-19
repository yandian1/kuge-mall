package com.kuge.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.CommonActivityEntity;
import com.kuge.mall.common.entity.CommonActivitySectionEntity;
import com.kuge.mall.common.entity.CommonActivitySectionSpuEntity;
import com.kuge.mall.common.entity.CommonSpuEntity;
import com.kuge.mall.common.service.CommonActivitySectionService;
import com.kuge.mall.common.service.CommonActivitySectionSpuService;
import com.kuge.mall.common.service.CommonActivityService;
import com.kuge.mall.common.service.CommonSpuService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.GoodsUtils;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.ActivityService;
import com.kuge.mall.product.vo.ActivitylDetailVo;
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
    public R<ActivitylDetailVo> getActivity(Long id) {
        String activityKey = RedisKeyConstant.ACTIVITY_PREFIX + id;
        String activityString = stringRedisTemplate.opsForValue().get(activityKey);
        if (StringUtils.hasLength(activityString)) {
            ActivitylDetailVo activitylDetailVo = JSON.parseObject(activityString, ActivitylDetailVo.class);
            return R.success(activitylDetailVo);
        }

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
                            CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonActivitySectionSpuEntity.getSpuId());
                            if (commonSpuEntity == null || (commonSpuEntity.getStatus() == null || commonSpuEntity.getStatus() != 1))
                                return null;

                            ActivitylDetailVo.GoodsItem goodsItem = new ActivitylDetailVo.GoodsItem();
                            goodsItem.setId(commonSpuEntity.getId());
                            goodsItem.setName(commonSpuEntity.getName());
                            goodsItem.setIntro(commonSpuEntity.getIntro());
                            goodsItem.setImg(commonSpuEntity.getFirstImg());
                            goodsItem.setPrice(commonSpuEntity.getPrice());
                            goodsItem.setLinePrice(commonSpuEntity.getLinePrice());
                            return goodsItem;
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                        section.setGoodsList(goodList);
                    }
                    return section;
                }).collect(Collectors.toList())
            );
        }

        stringRedisTemplate.opsForValue().set(activityKey, JSON.toJSONString(activitylDetailVo));

        return R.success(activitylDetailVo);
    }
}
