package com.kuge.mall.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.admin.vo.HomePageAddVo;
import com.kuge.mall.admin.vo.HomePageUpdateVo;
import com.kuge.mall.admin.vo.HomePageVo;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.CommonHomePageEntity;
import com.kuge.mall.common.entity.CommonSeckillTimeEntity;
import com.kuge.mall.common.entity.CommonSeckillTimeSpuEntity;
import com.kuge.mall.common.entity.CommonSpuEntity;
import com.kuge.mall.common.service.CommonHomePageService;
import com.kuge.mall.common.service.CommonSeckillTimeService;
import com.kuge.mall.common.service.CommonSeckillTimeSpuService;
import com.kuge.mall.common.service.CommonSpuService;
import com.kuge.mall.common.utils.R;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.kuge.mall.admin.service.HomePageService;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-20 15:33:52
 */
@Service("homePageService")
public class HomePageServiceImpl  implements HomePageService {

    @Resource
    private CommonHomePageService commonHomePageService;

    @Resource
    private CommonSeckillTimeService commonSeckillTimeService;

    @Resource
    private CommonSeckillTimeSpuService commonSeckillTimeSpuService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Override
    public R<Void> add(HomePageAddVo homePageAddVo) {
        CommonHomePageEntity homePageEntity = new CommonHomePageEntity();
        String detail = formatDetail(homePageAddVo.getDetail());
        homePageEntity.setDetail(detail);

        if (!commonHomePageService.save(homePageEntity)) {
            return R.fail("添加首页失败");
        }

        return R.success("添加首页成功");
    }

    @Override
    public R<Void> updateHomePage(HomePageUpdateVo homePageUpdateVo) {
        CommonHomePageEntity homePageEntity = commonHomePageService.getById(homePageUpdateVo.getId());
        if (homePageEntity == null) {
            return R.fail("首页配置不存在");
        }

        String detail = formatDetail(homePageUpdateVo.getDetail());
        homePageEntity.setDetail(detail);

        if (!commonHomePageService.updateById(homePageEntity)) {
            return R.fail("更新首页失败");
        }

        stringRedisTemplate.delete(RedisKeyConstant.HOME_PAGE);


        return R.success("更新首页成功");
    }

    @Override
    public R<HomePageVo> getHomePage() {
        List<CommonHomePageEntity> list = commonHomePageService.list();
        HomePageVo homePageVo = new HomePageVo();

        if(CollectionUtils.isEmpty(list)) {
            return R.success(homePageVo);
        }

        CommonHomePageEntity homePageEntity = list.get(0);
        BeanUtils.copyProperties(homePageEntity, homePageVo);

        return R.success(homePageVo);
    }

    private String formatDetail(String detail) {
        Map detailMap = JSON.parseObject(detail, Map.class);

        Object seckillListObj = detailMap.get("seckillList");
        if (seckillListObj != null) {
            JSONArray seckillList = (JSONArray)seckillListObj;
            Object firstSeckillObj = seckillList.get(0);
            if (firstSeckillObj != null) {
                JSONObject firstSeckill = (JSONObject)firstSeckillObj;

                Seckill seckill = new Seckill();
                Long seckillId = Long.valueOf(firstSeckill.get("id") + "");
                seckill.setId(seckillId);
                List<CommonSeckillTimeEntity> commonSeckillTimeEntities = commonSeckillTimeService.list(
                    new QueryWrapper<CommonSeckillTimeEntity>().eq("seckill_id", seckillId)
                );
                if (!CollectionUtils.isEmpty(commonSeckillTimeEntities)) {
                    List<Long> seckillTimeIds = commonSeckillTimeEntities.stream().map(CommonSeckillTimeEntity::getId).collect(Collectors.toList());
                    List<CommonSeckillTimeSpuEntity> commonSeckillTimeSpuEntities = commonSeckillTimeSpuService.list(
                        new QueryWrapper<CommonSeckillTimeSpuEntity>().in("seckill_time_id", seckillTimeIds)
                    );
                    if (!CollectionUtils.isEmpty(commonSeckillTimeSpuEntities)) {
                        seckill.setGoodsList(
                            commonSeckillTimeSpuEntities.subList(0, 5).stream().map(commonSeckillTimeSpuEntity -> {
                                GoodsItem goodsItem = new GoodsItem();
                                CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSeckillTimeSpuEntity.getSpuId());
                                if (commonSpuEntity == null) return null;
                                BeanUtils.copyProperties(commonSpuEntity, goodsItem);
                                return goodsItem;
                            }).filter(Objects::nonNull).collect(Collectors.toList())
                        );
                    }
                }

                detailMap.put("seckill", seckill);
            }
        }

        return JSON.toJSONString(detailMap);
    }

    @Data
    static class Seckill {
        /**
         * 秒杀id
         */
        private Long id;

        /**
         * 轮播图跳转地址
         */
        private List<GoodsItem> goodsList;
    }

    @Data
    static class GoodsItem {
        /**
         * 商品id
         */
        private Long id;

        /**
         * 商品首图
         */
        private String firstImg;

        /**
         * 商品名称
         */
        private String name;

        /**
         * 商品价格
         */
        private BigDecimal price;

        /**
         * 商品价格
         */
        private BigDecimal linePrice;
    }
}