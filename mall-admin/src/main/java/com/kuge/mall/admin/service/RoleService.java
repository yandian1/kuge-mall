package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import java.util.List;

/**
 * created by xbxie on 2024/4/25
 */
public interface RoleService {
    R<Void> add(RoleAddVo roleAddVo);

    R<Void> del(Long id);

    R<Void> updateRole(RoleUpdateVo roleUpdateVo);

    R<PageData<RolePageItemVo>> pageList(RolePageVo rolePageVo);

    R<RoleDetailVo> getRole(Long id);

    R<List<RoleListItemVo>> list();
}
