package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

import java.util.List;

/**
 * created by xbxie on 2024/4/25
 */
public interface MenuService {
    R<Void> add(MenuAddVo menuAddVo);

    R<Void> del(Long id);

    R<PageData<MenuPageResVo>> pageList(MenuPageReqVo menuPageReqVo);

    R<Void> updateMenu(MenuUpdateVo menuUpdateVo);

    R<MenuDetailVo> getMenu(Long id);

    R<List<MenuTreeItemVo>> getMenuTree();
}
