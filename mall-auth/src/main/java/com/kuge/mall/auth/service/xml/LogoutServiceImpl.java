package com.kuge.mall.auth.service.xml;

import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.AuthConstant;
import com.kuge.mall.auth.service.LogoutService;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.R;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
@Service("logoutService")
public class LogoutServiceImpl implements LogoutService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<Void> userLogout(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        if (tokenDto == null) {
            return R.success("退出登录成功");
        }

        Long id = tokenDto.getId();
        if (id == null) {
            return R.success("退出登录成功");
        }

        String key = AuthConstant.REDIS_TOKEN_ADMIN_PREFIX + id;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            return R.success("退出登录成功");
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.delete(key))) {
            return R.fail("退出登录失败");
        }

        return R.success("退出登录成功");
    }

    @Override
    public R<Void> memberLogout(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        if (tokenDto == null) {
            return R.fail("退出登录失败");
        }

        Long id = tokenDto.getId();
        if (id == null) {
            return R.fail("退出登录失败");
        }

        String key = AuthConstant.REDIS_TOKEN_MEMBER_PREFIX + id;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            return R.success("退出登录成功");
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.delete(key))) {
            return R.fail("退出登录失败");
        }

        return R.success("退出登录成功");
    }
}
