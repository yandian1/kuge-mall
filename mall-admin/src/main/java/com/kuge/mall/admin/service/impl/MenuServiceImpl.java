package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.MenuService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.entity.CommonRoleMenuRelEntity;
import com.kuge.mall.common.service.CommonMenuService;
import com.kuge.mall.common.service.CommonRoleMenuRelService;
import com.kuge.mall.common.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024/4/25
 */
@Service("menuService")
public class MenuServiceImpl  implements MenuService {
    @Resource
    private CommonMenuService commonMenuService;
    
    @Resource
    private CommonRoleMenuRelService commonRoleMenuRelService;

    @Override
    public R<Void> add(MenuAddVo menuAddVo) {
        List<CommonMenuEntity> list = commonMenuService.list(new QueryWrapper<CommonMenuEntity>().eq("pid", menuAddVo.getPid()).eq("name", menuAddVo.getName()).or().eq("path", menuAddVo.getPath()));

        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getName(), menuAddVo.getName()))) {
                return R.fail("菜单名重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getPath(), menuAddVo.getPath()))) {
                return R.fail("菜单路径重复");
            }
        }

        if (menuAddVo.getSort() == null) {
            menuAddVo.setSort(1);
        }

        CommonMenuEntity menuEntity = new CommonMenuEntity();
        BeanUtils.copyProperties(menuAddVo, menuEntity);
        return commonMenuService.save(menuEntity) ? R.success("添加菜单成功") : R.fail("添加菜单失败");
    }

    @Transactional
    @Override
    public R<Void> del(Long id) {
        // 菜单不存在
        if (!commonMenuService.exists(new QueryWrapper<CommonMenuEntity>().eq("id", id))) {
            return R.fail("菜单不存在");
        }

        // 删除菜单
        if (!commonMenuService.removeById(id)) {
            return R.fail("删除菜单失败");
        }


        // 删除菜单角色关系，这样之前拥有该菜单的角色就不在拥有该菜单了
        QueryWrapper<CommonRoleMenuRelEntity> wrapper = new QueryWrapper<CommonRoleMenuRelEntity>().eq("menu_id", id);
        if (commonRoleMenuRelService.exists(wrapper)) {
            if (!commonRoleMenuRelService.remove(wrapper)) {
                throw new CustomException("删除菜单角色失败");
            }
        }


        return R.success("删除菜单成功");
    }

    @Override
    public R<PageData<MenuPageResVo>> pageList(MenuPageReqVo menuPageReqVo) {
        String name = menuPageReqVo.getName();

        if (StringUtils.hasLength(name)) {
            QueryWrapper<CommonMenuEntity> wrapper = new QueryWrapper<>();

            if (StringUtils.hasLength(name)) {
                wrapper.like("name", name);
            }

            Page<CommonMenuEntity> page = commonMenuService.page(new Page<>(menuPageReqVo.getPageNum(), menuPageReqVo.getPageSize()), wrapper);
            PageData<MenuPageResVo> pageData = PageData.getPageData(page, MenuPageResVo.class);

            return R.success(pageData);
        } else {
            List<MenuPageResVo> menuPageResVos = TreeUtils.genTree(null, commonMenuService.list(), MenuPageResVo.class, true);
            PageData<MenuPageResVo> pageData = PageData.getPageData(menuPageReqVo.getPageNum(), menuPageReqVo.getPageSize(), menuPageResVos);

            return R.success(pageData);
        }
    }

    @Override
    public R<Void> updateMenu(MenuUpdateVo menuUpdateVo) {

        // 更新的菜单不存在
        Long id = menuUpdateVo.getId();
        if (commonMenuService.getById(id) == null) {
            return R.fail("菜单不存在");
        }

        Long pid = menuUpdateVo.getPid();
        if (Objects.equals(id, pid)) {
            return R.fail("无法将自身设置为父节点");
        }

        // 菜单名/菜单路径重复
        List<CommonMenuEntity> list = commonMenuService.list(
            new QueryWrapper<CommonMenuEntity>()
                .ne("id", id)
                .eq("pid", menuUpdateVo.getPid())
                .and(i -> i.eq("name", menuUpdateVo.getName()).or().eq("path", menuUpdateVo.getPath()))
        );
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getName(), menuUpdateVo.getName()))) {
                return R.fail("菜单名重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getPath(), menuUpdateVo.getPath()))) {
                return R.fail("菜单路径重复");
            }
        }

        if (menuUpdateVo.getSort() == null) {
            menuUpdateVo.setSort(1);
        }


        CommonMenuEntity menuEntity = new CommonMenuEntity();
        BeanUtils.copyProperties(menuUpdateVo, menuEntity);

        if (!commonMenuService.updateById(menuEntity)) {
            return R.fail("更新菜单失败");
        }

        return R.success("更新菜单成功");
    }

    @Override
    public R<MenuDetailVo> getMenu(Long id) {
        CommonMenuEntity menuEntity = commonMenuService.getById(id);
        if (menuEntity == null) {
            throw new CustomException("菜单不存在");
        }

        MenuDetailVo menuDetailVo = new MenuDetailVo();
        BeanUtils.copyProperties(menuEntity, menuDetailVo);

        List<Long> pids = TreeUtils.genPids(menuEntity.getId(), commonMenuService.list());
        menuDetailVo.setPids(pids);

        return R.success(menuDetailVo);
    }

    @Override
    public R<List<MenuTreeItemVo>> getMenuTree() {
        List<CommonMenuEntity> list = commonMenuService.list();
        if (CollectionUtils.isEmpty(list)) {
            return R.success(new ArrayList<>());
        }

        List<MenuTreeItemVo> menuTreeItemVos = TreeUtils.genTree(null, list, MenuTreeItemVo.class, true);
        return R.success(menuTreeItemVos);
    }
}
