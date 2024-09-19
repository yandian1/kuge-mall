package com.kuge.mall.auth.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.auth.vo.AdminInfoVo;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.entity.CommonMenuEntity;
import com.kuge.mall.common.entity.CommonRoleMenuRelEntity;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.entity.CommonUserRoleRelEntity;
import com.kuge.mall.common.service.CommonMenuService;
import com.kuge.mall.common.service.CommonRoleMenuRelService;
import com.kuge.mall.common.service.CommonUserRoleRelService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/7/30
 */
@Component
public class UserUtils {

    @Resource
    private CommonMenuService commonMenuService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CommonUserRoleRelService commonUserRoleRelService;

    @Resource
    private CommonRoleMenuRelService commonRoleMenuRelService;

    public AdminInfoVo genAdminInfo(CommonUserEntity commonUserEntity) {
        if (commonUserEntity == null) {
            throw new CustomException("用户不存在");
        }

        if (commonUserEntity.getStatus() == 0) {
            throw new CustomException("用户被禁用");
        }


        AdminInfoVo adminInfoVo = new AdminInfoVo();

        // 生成 token
        TokenDto tokenDto = new TokenDto();
        BeanUtils.copyProperties(commonUserEntity, tokenDto);
        adminInfoVo.setToken(JwtUtils.createToken(tokenDto));

        // 生成 user
        AdminInfoVo.User user = new AdminInfoVo.User();
        BeanUtils.copyProperties(commonUserEntity, user);
        adminInfoVo.setUser(user);

        // 生成 roleIds
        List<Long> roleIds = commonUserRoleRelService.list(new QueryWrapper<CommonUserRoleRelEntity>().eq("user_id", commonUserEntity.getId())).stream().map(CommonUserRoleRelEntity::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            throw new CustomException("您没有可访问的菜单，请联系管理员设置~");
        }
        adminInfoVo.setRoleIds(roleIds);

        // 生成 menus
        List<CommonMenuEntity> commonMenuEntities = commonMenuService.listByIds(
            commonRoleMenuRelService.list(
                new QueryWrapper<CommonRoleMenuRelEntity>().in("role_id", roleIds)
            )
                .stream().map(CommonRoleMenuRelEntity::getMenuId).collect(Collectors.toList())
        );
        if (CollectionUtils.isEmpty(commonMenuEntities)) {
            throw new CustomException("您没有可访问的菜单，请联系管理员设置~");
        }

        List<AdminInfoVo.Menu> menus = TreeUtils.genTree(null, commonMenuEntities, AdminInfoVo.Menu.class, true);
        if (CollectionUtils.isEmpty(menus)) {
            throw new CustomException("您没有可访问的菜单，请联系管理员设置~");
        }
        adminInfoVo.setMenus(menus);

        return adminInfoVo;
    }
}
