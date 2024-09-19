package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

/**
 * created by xbxie on 2024/5/21
 */
public interface ActivityService {
    R<Void> add(ActivityAddVo activityAddVo);

    R<Void> updateActivity(ActivityUpdateVo activityUpdateVo);

    R<Void> del(Long id);

    R<PageData<ActivitylPageItemVo>> pageList(ActivityPageReqVo activityPageReqVo);

    R<ActivitylDetailVo> getActivity(Long id);
}
