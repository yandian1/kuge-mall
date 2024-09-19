package com.kuge.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.CommonSeckillEntity;
import com.kuge.mall.common.entity.CommonSeckillTimeEntity;
import com.kuge.mall.common.entity.CommonSeckillTimeSpuEntity;
import com.kuge.mall.common.entity.CommonSpuEntity;
import com.kuge.mall.common.service.CommonSeckillService;
import com.kuge.mall.common.service.CommonSeckillTimeService;
import com.kuge.mall.common.service.CommonSeckillTimeSpuService;
import com.kuge.mall.common.service.CommonSpuService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.SeckillService;
import com.kuge.mall.product.vo.SeckillDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
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
    public R<SeckillDetailVo> getSeckill(Long id) {
        String seckillKey = RedisKeyConstant.SECKILL_PREFIX + id;
        String seckillString = stringRedisTemplate.opsForValue().get(seckillKey);
        if (StringUtils.hasLength(seckillString)) {
            SeckillDetailVo seckillDetailVo = JSON.parseObject(seckillString, SeckillDetailVo.class);
            return R.success(seckillDetailVo);
        }

        CommonSeckillEntity commonSeckillEntity = commonSeckillService.getById(id);
        if (commonSeckillEntity == null) {
            throw new CustomException("秒杀活动不存在");
        }

        SeckillDetailVo seckillDetailVo = new SeckillDetailVo();
        BeanUtils.copyProperties(commonSeckillEntity, seckillDetailVo);

        // 时间段及商品
        QueryWrapper<CommonSeckillTimeEntity> queryWrapper = new QueryWrapper<CommonSeckillTimeEntity>().eq("seckill_id", commonSeckillEntity.getId());
        queryWrapper.orderByAsc("start_time");

        List<SeckillDetailVo.Time> timeList = commonSeckillTimeService.list(queryWrapper)
            .stream()
            .filter(commonSeckillTimeEntity -> {
                LocalDateTime endTime = commonSeckillTimeEntity.getEndTime();
                LocalDateTime now = LocalDateTime.now();
                return !(now.equals(endTime) || now.isAfter(endTime));
            })
            .map(commonSeckillTimeEntity -> {
                SeckillDetailVo.Time time = new SeckillDetailVo.Time();

                LocalDateTime startTime = commonSeckillTimeEntity.getStartTime();
                LocalDateTime endTime = commonSeckillTimeEntity.getEndTime();
                LocalDateTime now = LocalDateTime.now();

                if ((now.equals(startTime) || now.isAfter(startTime)) && now.isBefore(endTime)) {
                    time.setType("opening");
                    time.setSurplus(Duration.between(now, endTime).toMillis());
                }

                if (startTime.isAfter(now)) {
                    time.setType("notOpen");
                    time.setStartTime(startTime);
                }

                time.setSpuList(
                    commonSeckillTimeSpuService.list(new QueryWrapper<CommonSeckillTimeSpuEntity>().eq("seckill_time_id", commonSeckillTimeEntity.getId()))
                        .stream().map(commonSeckillTimeSpuEntity -> {
                            CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSeckillTimeSpuEntity.getSpuId());
                            if (commonSpuEntity == null || (commonSpuEntity.getStatus() == null || commonSpuEntity.getStatus() != 1))
                                return null;

                            SeckillDetailVo.Spu spu = new SeckillDetailVo.Spu();
                            spu.setId(commonSpuEntity.getId());
                            spu.setName(commonSpuEntity.getName());
                            spu.setIntro(commonSpuEntity.getIntro());
                            spu.setImg(commonSpuEntity.getFirstImg());
                            spu.setPrice(commonSpuEntity.getPrice());
                            spu.setLinePrice(commonSpuEntity.getLinePrice());


                            return spu;
                        }).collect(Collectors.toList())
                );

                return time;
            }).collect(Collectors.toList());

        seckillDetailVo.setTimeList(timeList);

        stringRedisTemplate.opsForValue().set(seckillKey, JSON.toJSONString(seckillDetailVo));

        return R.success(seckillDetailVo);
    }
}