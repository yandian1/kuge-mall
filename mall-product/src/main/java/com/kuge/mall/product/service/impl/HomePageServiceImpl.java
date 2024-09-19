package com.kuge.mall.product.service.impl;

import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.vo.HomePageVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.kuge.mall.product.service.HomePageService;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;

/**
 * created by xbxie on 2024-05-20 23:07:48
 */
@Service("homePageService")
public class HomePageServiceImpl implements HomePageService {

    @Resource
    private CommonHomePageService commonHomePageService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<HomePageVo> getHomePage() {
        HomePageVo homePageVo = new HomePageVo();

        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisKeyConstant.HOME_PAGE))) {
            String detail = stringRedisTemplate.opsForValue().get(RedisKeyConstant.HOME_PAGE);
            homePageVo.setDetail(detail);
            return R.success(homePageVo);
        }

        List<CommonHomePageEntity> list = commonHomePageService.list();
        if (CollectionUtils.isEmpty(list)) {
            return R.success(new HomePageVo());
        }

        String detail = list.get(0).getDetail();
        homePageVo.setDetail(detail);

        stringRedisTemplate.opsForValue().set(RedisKeyConstant.HOME_PAGE, detail);

        return R.success(homePageVo);
    }
}