package com.kuge.mall.auth.service.xml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.auth.feign.CartRedisFeignService;
import com.kuge.mall.auth.utils.UserUtils;
import com.kuge.mall.auth.vo.*;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.AuthConstant;
import com.kuge.mall.auth.service.LoginService;
import com.kuge.mall.common.constant.SmsConstant;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/23
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private CartRedisFeignService cartRedisFeignService;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private CommonMemberService commonMemberService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserUtils userUtils;

    @Override
    public R<AdminInfoVo> userLogin(LoginAdminReqVo loginAdminReqVo) {
        CommonUserEntity commonUserEntity = commonUserService.getOne(new QueryWrapper<CommonUserEntity>().eq("account", loginAdminReqVo.getAccount()), false);

        if (commonUserEntity == null) {
            throw new CustomException("用户不存在");
        }

        // 判断密码是否相等
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginAdminReqVo.getPassword(), commonUserEntity.getPassword())) {
            throw new CustomException("密码错误");
        }

        AdminInfoVo adminInfoVo = userUtils.genAdminInfo(commonUserEntity);

        // 生成 token
        TokenDto tokenDto = new TokenDto();
        BeanUtils.copyProperties(commonUserEntity, tokenDto);
        String token = JwtUtils.createToken(tokenDto);
        adminInfoVo.setToken(token);

        // 将 token 存入 redis
        stringRedisTemplate.opsForValue().set(AuthConstant.REDIS_TOKEN_ADMIN_PREFIX + commonUserEntity.getId().toString(), token);

        return R.success(adminInfoVo);
    }

    @Override
    public R<LoginMemberResVo> memberLogin(LoginMemberReqVo loginMemberReqVo) {
        // 判断验证码合法性
        String redisKey = SmsConstant.REDIS_VERIFY_CODE_PREFIX + loginMemberReqVo.getAccount();
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(redisKey))) {
            throw new CustomException("验证码无效");
        }

        // 根据用户账号查看数据库中是否有对应的用户
        CommonMemberEntity commonMemberEntity = commonMemberService.getOne(new QueryWrapper<CommonMemberEntity>().eq("account", loginMemberReqVo.getAccount()));
        if (commonMemberEntity == null) {
            // 注册账号
            commonMemberEntity = new CommonMemberEntity();
            commonMemberEntity.setAccount(loginMemberReqVo.getAccount());
            commonMemberEntity.setName(UserNameUtils.genUniqueName());
            if (!commonMemberService.save(commonMemberEntity)) {
                throw new CustomException("登录失败");
            }
        }


        // 生成 token ，并存入 redis
        TokenDto tokenDto = new TokenDto();
        BeanUtils.copyProperties(commonMemberEntity, tokenDto);
        String token = JwtUtils.createToken(tokenDto);

        LoginMemberResVo.User user = new LoginMemberResVo.User();
        BeanUtils.copyProperties(commonMemberEntity, user);

        LoginMemberResVo loginMemberResVo = new LoginMemberResVo();
        loginMemberResVo.setToken(token);
        loginMemberResVo.setUser(user);
        stringRedisTemplate.opsForValue().set(AuthConstant.REDIS_TOKEN_MEMBER_PREFIX + commonMemberEntity.getId().toString(), token);

        // 购物车中选中的商品数量
        R<Integer> integerR = cartRedisFeignService.getCountByMemberId(user.getId());
        loginMemberResVo.setCartCount(integerR.getData());

        // 删除Redis中验证码
        if (Boolean.FALSE.equals(stringRedisTemplate.delete(redisKey))) {
            throw new CustomException("登录失败");
        }

        return R.success(loginMemberResVo);
    }
}
