package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.UserService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.constant.AuthConstant;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.entity.CommonUserRoleRelEntity;
import com.kuge.mall.common.service.CommonUserRoleRelService;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private CommonUserService commonUserService;

    @Resource
    private CommonUserRoleRelService commonUserRoleRelService;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public R<Void> add(UserAddVo userAddVo) {
        // 用户名或者用户账号重复
        List<CommonUserEntity> list = commonUserService.list(new QueryWrapper<CommonUserEntity>().eq("name", userAddVo.getName()).or().eq("account", userAddVo.getAccount()));
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getName(), userAddVo.getName()))) {
                return R.fail("用户名重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getAccount(), userAddVo.getAccount()))) {
                return R.fail("用户账号重复");
            }
        }

        // 编码密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userAddVo.setPassword(encoder.encode(userAddVo.getPassword().trim()));

        // 插入用户
        CommonUserEntity userEntity = new CommonUserEntity();
        BeanUtils.copyProperties(userAddVo, userEntity);

        if (!commonUserService.save(userEntity)) {
            return R.fail("添加用户失败");
        }

        // 插入角色
        Long userId = userEntity.getId();
        List<Long> roleIds = userAddVo.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            if (
                !commonUserRoleRelService.saveBatch(
                    roleIds.stream().map(roleId -> {
                        CommonUserRoleRelEntity userRoleRelEntity = new CommonUserRoleRelEntity();
                        userRoleRelEntity.setUserId(userId);
                        userRoleRelEntity.setRoleId(roleId);
                        return userRoleRelEntity;
                    }).collect(Collectors.toList())
                )
            ) {
                throw  new CustomException("添加角色失败");
            }
        }

        return R.success("添加用户成功");
    }

    @Transactional
    @Override
    public R<Void> del(Long id) {
        // 用户不存在
        if (!commonUserService.exists(new QueryWrapper<CommonUserEntity>().eq("id", id))) {
            return R.fail("用户不存在");
        }

        // 删除用户
        if (!commonUserService.removeById(id)) {
            return R.fail("删除用户失败");
        }


        // 删除用户角色关系
        QueryWrapper<CommonUserRoleRelEntity> wrapper = new QueryWrapper<CommonUserRoleRelEntity>().eq("user_id", id);
        if (commonUserRoleRelService.exists(wrapper)) {
            if (!commonUserRoleRelService.remove(wrapper)) {
                throw new CustomException("删除用户角色失败");
            }
        }


        return R.success("删除用户成功");
    }

    @Transactional
    @Override
    public R<Void> updateUser(UserUpdateVo userUpdateVo) {
        // 更新的用户不存在
        Long id = userUpdateVo.getId();
        if (commonUserService.getById(id) == null) {
            return R.fail("用户不存在");
        }

        // 用户名或者用户账号重复
        List<CommonUserEntity> list = commonUserService.list(
            new QueryWrapper<CommonUserEntity>()
                .ne("id", id)
                .and(i -> i.eq("name", userUpdateVo.getName()).or().eq("account", userUpdateVo.getAccount()))
        );
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(item -> Objects.equals(item.getName(), userUpdateVo.getName()))) {
                return R.fail("用户名重复");
            }
            if (list.stream().anyMatch(item -> Objects.equals(item.getAccount(), userUpdateVo.getAccount()))) {
                return R.fail("用户账号重复");
            }
        }

        CommonUserEntity userEntity = new CommonUserEntity();
        BeanUtils.copyProperties(userUpdateVo, userEntity);

        if (!commonUserService.updateById(userEntity)) {
            return R.fail("更新用户失败");
        }

        // 更新用户的角色
        // 先删除用户的旧角色
        QueryWrapper<CommonUserRoleRelEntity> userRoleQuery = new QueryWrapper<CommonUserRoleRelEntity>().eq("user_id", id);
        if (commonUserRoleRelService.exists(userRoleQuery)) {
            if (!commonUserRoleRelService.remove(userRoleQuery)) {
                return R.fail("更新用户失败");
            }
        }
        // 添加新角色
        List<Long> roleIds = userUpdateVo.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            if (
                !commonUserRoleRelService.saveBatch(roleIds.stream().map(roleId -> {
                    CommonUserRoleRelEntity userRoleRelEntity = new CommonUserRoleRelEntity();
                    userRoleRelEntity.setUserId(id);
                    userRoleRelEntity.setRoleId(roleId);
                    return userRoleRelEntity;
                }).collect(Collectors.toList()))
            ) {
                throw new CustomException("更新用户角色失败");
            }
        }

        // 删除 redis 中的 token
        String key = AuthConstant.REDIS_TOKEN_ADMIN_PREFIX + userUpdateVo.getId();
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key)) && Boolean.FALSE.equals(stringRedisTemplate.delete(key))) {
            return R.fail("更新用户失败");
        }

        return R.success("更新用户成功");
    }

    @Override
    public R<PageData<UserPageItemVo>> pageList(UserPageVo userPageVo) {
        String name = userPageVo.getName();
        String account = userPageVo.getAccount();

        QueryWrapper<CommonUserEntity> wrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.hasLength(account)) {
            wrapper.like("account", account);
        }

        wrapper.ne("account", "18755781039");

        Page<CommonUserEntity> res = commonUserService.page(new Page<>(userPageVo.getPageNum(), userPageVo.getPageSize()), wrapper);
        PageData<UserPageItemVo> pageData = PageData.getPageData(res, UserPageItemVo.class);

        return R.success(pageData);
    }

    @Override
    public R<Void> changeStatus(UserStatusVo userStatusVo) {
        CommonUserEntity userEntity = commonUserService.getById(userStatusVo.getId());
        if (userEntity == null) {
            return R.fail("用户不存在");
        }

        BeanUtils.copyProperties(userStatusVo, userEntity);

        // 禁用用户，删除 redis 缓存
        if (userStatusVo.getStatus() == 0) {
            String key = AuthConstant.REDIS_TOKEN_ADMIN_PREFIX + userStatusVo.getId();
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key)) && Boolean.FALSE.equals(stringRedisTemplate.delete(key))) {
                return R.fail("禁用用户失败");
            }
        }

        return commonUserService.updateById(userEntity) ? R.success() : R.fail();
    }

    @Override
    public R<UserDetailVo> getUser(Long id) {
        CommonUserEntity userEntity = commonUserService.getById(id);
        if (userEntity == null) {
            throw new CustomException("用户不存在");
        }

        UserDetailVo userDetailVo = new UserDetailVo();
        BeanUtils.copyProperties(userEntity, userDetailVo);

        List<CommonUserRoleRelEntity> commonUserRoleRelEntities = commonUserRoleRelService.list(new QueryWrapper<CommonUserRoleRelEntity>().eq("user_id", id));
        if (!CollectionUtils.isEmpty(commonUserRoleRelEntities)) {
            List<Long> roleIds = commonUserRoleRelEntities.stream().map(CommonUserRoleRelEntity::getRoleId).collect(Collectors.toList());
            userDetailVo.setRoleIds(roleIds);
        }

        return R.success(userDetailVo);
    }

    @Override
    public R<Void> updatePwd(UserUpdatePwdVo userUpdatePwdVo) {
        CommonUserEntity commonUserEntity = commonUserService.getById(userUpdatePwdVo.getId());
        if (commonUserEntity == null) {
            return R.fail("用户不存在");
        }

        // 判断密码是否相等
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(userUpdatePwdVo.getOldPwd(), commonUserEntity.getPassword())) {
            return R.fail("密码错误");
        }

        if (!Objects.equals(userUpdatePwdVo.getNewPwd(), userUpdatePwdVo.getConfirmPwd())) {
            return R.fail("新密码和确认密码不同");
        }

        commonUserEntity.setPassword(userUpdatePwdVo.getNewPwd());
        if (!commonUserService.updateById(commonUserEntity)) {
            return R.fail("设置密码失败");
        }

        return R.success("设置密码成功");
    }
}
