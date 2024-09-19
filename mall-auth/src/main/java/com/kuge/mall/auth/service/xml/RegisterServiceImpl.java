package com.kuge.mall.auth.service.xml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.auth.vo.RegisterUserReqVo;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.auth.service.RegisterService;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.auth.vo.RegisterMemberReqVo;
import com.kuge.mall.auth.vo.RegisterMemberResVo;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.service.CommonMemberService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/23
 */
@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private CommonMemberService commonMemberService;

    @Resource
    private CommonUserService commonUserService;

    @Override
    public R<RegisterMemberResVo> memberRegister(RegisterMemberReqVo registerMemberReqVo) {
        // 账号重复
        if (commonMemberService.exists(new QueryWrapper<CommonMemberEntity>().eq("account", registerMemberReqVo.getAccount()))) {
            throw new CustomException("账号重复");
        }

        // 编码密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        registerMemberReqVo.setPassword(encoder.encode(registerMemberReqVo.getPassword().trim()));

        // 注册用户
        CommonMemberEntity commonMemberEntity = new CommonMemberEntity();
        BeanUtils.copyProperties(registerMemberReqVo, commonMemberEntity);

        if (!commonMemberService.save(commonMemberEntity)) {
            throw new CustomException("注册失败");
        }


        TokenDto tokenDto = new TokenDto();
        BeanUtils.copyProperties(commonMemberEntity, tokenDto);
        String token = JwtUtils.createToken(tokenDto);

        RegisterMemberResVo.User user = new RegisterMemberResVo.User();
        BeanUtils.copyProperties(commonMemberEntity, user);

        RegisterMemberResVo registerMemberResVo = new RegisterMemberResVo();
        registerMemberResVo.setToken(token);
        registerMemberResVo.setUser(user);

        return R.success(registerMemberResVo);
    }

    @Override
    public R<Void> userRegister(RegisterUserReqVo registerUserReqVo) {
        // 账号重复
        if (commonUserService.exists(new QueryWrapper<CommonUserEntity>().eq("account", registerUserReqVo.getAccount()))) {
            throw new CustomException("账号重复");
        }

        // 编码密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        registerUserReqVo.setPassword(encoder.encode(registerUserReqVo.getPassword().trim()));

        // 注册用户
        CommonUserEntity commonUserEntity = new CommonUserEntity();
        BeanUtils.copyProperties(registerUserReqVo, commonUserEntity);

        if (!commonUserService.save(commonUserEntity)) {
            throw new CustomException("注册失败");
        }

        return R.success();
    }
}
