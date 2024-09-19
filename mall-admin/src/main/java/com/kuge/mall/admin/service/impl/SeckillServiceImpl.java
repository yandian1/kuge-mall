package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.SeckillService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.CommonSeckillService;
import com.kuge.mall.common.service.CommonSeckillTimeService;
import com.kuge.mall.common.service.CommonSeckillTimeSpuService;
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
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {
    
    @Resource
    private CommonSeckillService commonSeckillService;

    @Resource
    private CommonSeckillTimeService commonSeckillTimeService;

    @Resource
    private CommonSeckillTimeSpuService commonSeckillTimeSpuService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Override
    public R<Void> add(SeckillAddVo seckillAddVo) {
        if (
            commonSeckillService.exists(new QueryWrapper<CommonSeckillEntity>().eq("name", seckillAddVo.getName()))
        ) {
            return R.fail("优秒杀活动名重复");
        }

        // 秒杀活动基本信息
        CommonSeckillEntity commonSeckillEntity = new CommonSeckillEntity();
        BeanUtils.copyProperties(seckillAddVo, commonSeckillEntity);

        if (!commonSeckillService.save(commonSeckillEntity)) {
            return R.fail("添加秒杀活动失败");
        }

        // 时间段
        List<SeckillAddVo.Time> timeList = seckillAddVo.getTimeList();
        if (!CollectionUtils.isEmpty(timeList)) {
            for (SeckillAddVo.Time time : timeList) {
                CommonSeckillTimeEntity commonSeckillTimeEntity = new CommonSeckillTimeEntity();
                commonSeckillTimeEntity.setSeckillId(commonSeckillEntity.getId());
                commonSeckillTimeEntity.setStartTime(time.getStartTime());
                commonSeckillTimeEntity.setEndTime(time.getEndTime());
                if (!commonSeckillTimeService.save(commonSeckillTimeEntity)) {
                    throw new CustomException("添加秒杀活动失败");
                }

                // 时间段商品
                List<CommonSeckillTimeSpuEntity> commonSeckillTimeSpuEntities = time.getSpuIds().stream().map(spuId -> {
                    CommonSeckillTimeSpuEntity commonSeckillTimeSpuEntity = new CommonSeckillTimeSpuEntity();
                    commonSeckillTimeSpuEntity.setSeckillTimeId(commonSeckillTimeEntity.getId());
                    commonSeckillTimeSpuEntity.setSpuId(spuId);
                    return commonSeckillTimeSpuEntity;
                }).collect(Collectors.toList());

                if (!commonSeckillTimeSpuService.saveBatch(commonSeckillTimeSpuEntities)) {
                    throw new CustomException("添加秒杀活动失败");
                }
            }
        }

        return R.success("添加秒杀活动成功");
    }

    @Override
    public R<Void> updateSeckill(SeckillUpdateVo seckillUpdateVo) {
        // 更新的秒杀活动不存在
        CommonSeckillEntity commonSeckillEntity = commonSeckillService.getById(seckillUpdateVo.getId());
        if (commonSeckillEntity == null) {
            return R.fail("秒杀活动不存在");
        }

        if (
            commonSeckillService.exists(
                new QueryWrapper<CommonSeckillEntity>()
                    .ne("id", seckillUpdateVo.getId())
                    .eq("name", seckillUpdateVo.getName())
            )
        ) {
            return R.fail("秒杀活动名重复");
        }

        BeanUtils.copyProperties(seckillUpdateVo, commonSeckillEntity);

        if (!commonSeckillService.updateById(commonSeckillEntity)) {
            return R.fail("编辑秒杀活动失败");
        }

        // 删除时间段及商品
        commonSeckillTimeService.list(new QueryWrapper<CommonSeckillTimeEntity>().eq("seckill_id", commonSeckillEntity.getId()))
            .stream().forEach(commonSeckillTimeEntity -> {
                Long timeId = commonSeckillTimeEntity.getId();
                commonSeckillTimeService.removeById(timeId);
                commonSeckillTimeSpuService.remove(new QueryWrapper<CommonSeckillTimeSpuEntity>().eq("seckill_time_id", timeId));
            });

        // 时间段
        List<SeckillAddVo.Time> timeList = seckillUpdateVo.getTimeList();
        if (!CollectionUtils.isEmpty(timeList)) {
            for (SeckillAddVo.Time time : timeList) {
                CommonSeckillTimeEntity commonSeckillTimeEntity = new CommonSeckillTimeEntity();
                commonSeckillTimeEntity.setSeckillId(seckillUpdateVo.getId());
                commonSeckillTimeEntity.setStartTime(time.getStartTime());
                commonSeckillTimeEntity.setEndTime(time.getEndTime());
                if (!commonSeckillTimeService.save(commonSeckillTimeEntity)) {
                    throw new CustomException("编辑秒杀活动失败");
                }

                // 时间段商品
                List<CommonSeckillTimeSpuEntity> commonSeckillTimeSpuEntities = time.getSpuIds().stream().map(spuId -> {
                    CommonSeckillTimeSpuEntity commonSeckillTimeSpuEntity = new CommonSeckillTimeSpuEntity();
                    commonSeckillTimeSpuEntity.setSeckillTimeId(commonSeckillTimeEntity.getId());
                    commonSeckillTimeSpuEntity.setSpuId(spuId);
                    return commonSeckillTimeSpuEntity;
                }).collect(Collectors.toList());

                if (!commonSeckillTimeSpuService.saveBatch(commonSeckillTimeSpuEntities)) {
                    throw new CustomException("编辑秒杀活动失败");
                }
            }
        }

        stringRedisTemplate.delete(RedisKeyConstant.SECKILL_PREFIX + seckillUpdateVo.getId());

        return R.success("编辑秒杀活动失败");
    }

    @Override
    public R<Void> del(Long id) {
        // 秒杀活动不存在
        if (commonSeckillService.getById(id) == null) {
            return R.fail("秒杀活动不存在");
        }

        // 删除秒杀活动
        if (!commonSeckillService.removeById(id)) {
            return R.fail("删除秒杀活动失败");
        }

        // 删除时间段及商品
        commonSeckillTimeService.list(new QueryWrapper<CommonSeckillTimeEntity>().eq("seckill_id", id))
            .stream().forEach(commonSeckillTimeEntity -> {
                Long timeId = commonSeckillTimeEntity.getId();
                commonSeckillTimeService.removeById(timeId);
                commonSeckillTimeSpuService.remove(new QueryWrapper<CommonSeckillTimeSpuEntity>().eq("seckill_time_id", timeId));
            });

        return R.success("删除秒杀活动成功");
    }

    @Override
    public R<PageData<SeckillPageItemVo>> pageList(SeckillPageReqVo seckillPageReqVo) {

        String name = seckillPageReqVo.getName();

        QueryWrapper<CommonSeckillEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");

        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }

        Page<CommonSeckillEntity> page = commonSeckillService.page(new Page<>(seckillPageReqVo.getPageNum(), seckillPageReqVo.getPageSize()), wrapper);
        PageData<SeckillPageItemVo> pageData = PageData.getPageData(page, SeckillPageItemVo.class);

        return R.success(pageData);
    }

    @Override
    public R<SeckillDetailVo> getSeckill(Long id) {
        CommonSeckillEntity commonSeckillEntity = commonSeckillService.getById(id);
        if (commonSeckillEntity == null) {
            throw new CustomException("秒杀活动不存在");
        }

        SeckillDetailVo seckillDetailVo = new SeckillDetailVo();
        BeanUtils.copyProperties(commonSeckillEntity, seckillDetailVo);

        // 时间段及商品
        List<SeckillDetailVo.Time> timeList = commonSeckillTimeService.list(new QueryWrapper<CommonSeckillTimeEntity>().eq("seckill_id", commonSeckillEntity.getId()))
            .stream().map(commonSeckillTimeEntity -> {
                SeckillDetailVo.Time time = new SeckillDetailVo.Time();

                time.setStartTime(commonSeckillTimeEntity.getStartTime());
                time.setEndTime(commonSeckillTimeEntity.getEndTime());

                List<SeckillDetailVo.Spu> spuList = commonSeckillTimeSpuService.list(new QueryWrapper<CommonSeckillTimeSpuEntity>().eq("seckill_time_id", commonSeckillTimeEntity.getId()))
                    .stream().map(commonSeckillTimeSpuEntity -> {
                        SeckillDetailVo.Spu spu = new SeckillDetailVo.Spu();
                        CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSeckillTimeSpuEntity.getSpuId());
                        if (commonSpuEntity == null || commonSpuEntity.getStatus() == null || commonSpuEntity.getStatus() != 1) {
                            return null;
                        }
                        BeanUtils.copyProperties(commonSpuEntity, spu);
                        spu.setStatusDesc(GoodsUtils.getDesc(commonSpuEntity.getStatus()));
                        return spu;
                    }).filter(Objects::nonNull).collect(Collectors.toList());

                time.setSpuList(spuList);

                return time;
            }).collect(Collectors.toList());

        seckillDetailVo.setTimeList(timeList);

        return R.success(seckillDetailVo);
    }
}
