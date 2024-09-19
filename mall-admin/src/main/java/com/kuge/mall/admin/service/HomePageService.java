package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.HomePageAddVo;
import com.kuge.mall.admin.vo.HomePageUpdateVo;
import com.kuge.mall.admin.vo.HomePageVo;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024-05-20 15:33:52
 */
public interface HomePageService {
    R<Void> add(HomePageAddVo homePageAddVo);

    R<Void> updateHomePage(HomePageUpdateVo homePageUpdateVo);

    R<HomePageVo> getHomePage();
}

