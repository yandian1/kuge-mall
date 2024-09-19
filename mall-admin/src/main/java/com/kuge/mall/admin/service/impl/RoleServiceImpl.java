package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.RoleService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonRoleEntity;
import com.kuge.mall.common.entity.CommonRoleMenuRelEntity;
import com.kuge.mall.common.service.CommonRoleMenuRelService;
import com.kuge.mall.common.service.CommonRoleService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/4/25
 */
@Service("roleService")
public class RoleServiceImpl  implements RoleService {
    @Resource
    private CommonRoleService commonRoleService;
    
    @Resource
    private CommonRoleMenuRelService commonRoleMenuRelService;

    @Override
    public R<Void> add(RoleAddVo roleAddVo) {
        // 角色名重复
        if (commonRoleService.exists(new QueryWrapper<CommonRoleEntity>().eq("name", roleAddVo.getName()))) {
            return R.fail("角色名重复");
        }

        // 插入角色
        CommonRoleEntity roleEntity = new CommonRoleEntity();
        BeanUtils.copyProperties(roleAddVo, roleEntity);
        if (!commonRoleService.save(roleEntity)) {
            return R.fail("添加角色失败");
        }

        // 插入菜单
        Long roleId = roleEntity.getId();
        List<Long> menuIds = roleAddVo.getMenuIds();
        if (!CollectionUtils.isEmpty(menuIds)) {
            if (
                !commonRoleMenuRelService.saveBatch(
                    menuIds.stream().map(menuId -> {
                        CommonRoleMenuRelEntity roleMenuRelEntity = new CommonRoleMenuRelEntity();
                        roleMenuRelEntity.setRoleId(roleId);
                        roleMenuRelEntity.setMenuId(menuId);
                        return roleMenuRelEntity;
                    }).collect(Collectors.toList())
                )
            ) {
                throw  new CustomException("添加角色失败");
            }
        }

        return R.success("添加角色成功");
    }

    @Transactional
    @Override
    public R<Void> del(Long id) {
        // 角色不存在
        if (!commonRoleService.exists(new QueryWrapper<CommonRoleEntity>().eq("id", id))) {
            return R.fail("角色不存在");
        }

        // 删除角色
        if (!commonRoleService.removeById(id)) {
            return R.fail("删除角色失败");
        }

        // 删除角色菜单关系
        QueryWrapper<CommonRoleMenuRelEntity> wrapper = new QueryWrapper<CommonRoleMenuRelEntity>().eq("role_id", id);
        if (commonRoleMenuRelService.exists(wrapper)) {
            if (!commonRoleMenuRelService.remove(wrapper)) {
                throw new CustomException("删除角色菜单失败");
            }
        }

        return R.success("删除角色成功");
    }

    @Override
    public R<Void> updateRole(RoleUpdateVo roleUpdateVo) {
        // 角色不存在
        Long id = roleUpdateVo.getId();
        if (commonRoleService.getById(id) == null) {
            return R.fail("角色不存在");
        }

        // 角色名重复
        if (!CollectionUtils.isEmpty(
            commonRoleService.list(
                new QueryWrapper<CommonRoleEntity>()
                    .ne("id", id)
                    .and(i -> i.eq("name", roleUpdateVo.getName()))
            )
        )) {
            return R.fail("角色名重复");
        }

        CommonRoleEntity roleEntity = new CommonRoleEntity();
        BeanUtils.copyProperties(roleUpdateVo, roleEntity);

        if (!commonRoleService.updateById(roleEntity)) {
            return R.fail("更新角色失败");
        }

        // 更新角色的菜单
        // 先删除角色的旧菜单
        QueryWrapper<CommonRoleMenuRelEntity> roleMenuQuery = new QueryWrapper<CommonRoleMenuRelEntity>().eq("role_id", id);
        if (commonRoleMenuRelService.exists(roleMenuQuery)) {
            if (!commonRoleMenuRelService.remove(roleMenuQuery)) {
                return R.fail("更新角色菜单失败");
            }
        }
        // 添加新菜单
        List<Long> menuIds = roleUpdateVo.getMenuIds();
        if (!CollectionUtils.isEmpty(menuIds)) {
            if (
                !commonRoleMenuRelService.saveBatch(menuIds.stream().map(menuId -> {
                    CommonRoleMenuRelEntity roleMenuRelEntity = new CommonRoleMenuRelEntity();
                    roleMenuRelEntity.setRoleId(id);
                    roleMenuRelEntity.setMenuId(menuId);
                    return roleMenuRelEntity;
                }).collect(Collectors.toList()))
            ) {
                throw new CustomException("更新角色菜单失败");
            }
        }

        return R.success("更新角色成功");
    }

    @Override
    public R<PageData<RolePageItemVo>> pageList(RolePageVo rolePageVo) {
        String name = rolePageVo.getName();

        QueryWrapper<CommonRoleEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }

        Page<CommonRoleEntity> res = commonRoleService.page(new Page<>(rolePageVo.getPageNum(), rolePageVo.getPageSize()), wrapper);
        PageData<RolePageItemVo> pageData = PageData.getPageData(res, RolePageItemVo.class);
        return R.success(pageData);
    }

    @Override
    public R<RoleDetailVo> getRole(Long id) {
        CommonRoleEntity roleEntity = commonRoleService.getById(id);
        if (roleEntity == null) {
            throw new CustomException("角色不存在");
        }

        RoleDetailVo roleDetailVo = new RoleDetailVo();
        BeanUtils.copyProperties(roleEntity, roleDetailVo);

        List<CommonRoleMenuRelEntity> roleMenuList = commonRoleMenuRelService.list(new QueryWrapper<CommonRoleMenuRelEntity>().eq("role_id", id));
        if (!CollectionUtils.isEmpty(roleMenuList)) {
            List<Long> menuIds = roleMenuList.stream().map(CommonRoleMenuRelEntity::getMenuId).collect(Collectors.toList());
            roleDetailVo.setMenuIds(menuIds);
        }
        return R.success(roleDetailVo);
    }

    @Override
    public R<List<RoleListItemVo>> list() {
        List<CommonRoleEntity> list = commonRoleService.list();
        if (CollectionUtils.isEmpty(list)) {
            R.success(new ArrayList<RoleListItemVo>());
        }

        List<RoleListItemVo> roleListItemVos = list.stream().map(item -> {
            RoleListItemVo roleListItemVo = new RoleListItemVo();
            BeanUtils.copyProperties(item, roleListItemVo);
            return roleListItemVo;
        }).collect(Collectors.toList());

        return R.success(roleListItemVos);
    }
}
