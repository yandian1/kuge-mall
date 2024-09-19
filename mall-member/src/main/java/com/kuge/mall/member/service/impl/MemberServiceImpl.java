package com.kuge.mall.member.service.impl;

import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.SmsConstant;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.service.CommonMemberService;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.service.MemberService;
import com.kuge.mall.member.vo.AccountReqVo;
import com.kuge.mall.member.vo.NameReqVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * created by xbxie on 2024/5/23
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CommonMemberService commonMemberService;

    @Override
    public R<Void> setName(NameReqVo nameReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        CommonMemberEntity commonMemberEntity = commonMemberService.getById(tokenDto.getId());

        if (commonMemberEntity == null) {
            return R.fail("未查找到用户");
        }

        commonMemberEntity.setName(nameReqVo.getName());
        if (!commonMemberService.updateById(commonMemberEntity)) {
            return R.fail("设置失败");
        }

        return R.success();
    }

    @Override
    public R<Void> setAccount(AccountReqVo accountReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        CommonMemberEntity commonMemberEntity = commonMemberService.getById(tokenDto.getId());

        if (commonMemberEntity == null) {
            return R.fail("未查找到用户");
        }

        // 删除 redis 里的验证码
        String key = SmsConstant.REDIS_VERIFY_CODE_PREFIX + accountReqVo.getAccount();
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            return R.fail("验证码无效");
        }

        String code = stringRedisTemplate.opsForValue().get(key);
        if (!Objects.equals(code, accountReqVo.getCode())) {
            return R.fail("验证码无效");
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.delete(key))) {
            return R.fail("设置失败");
        }

        commonMemberEntity.setAccount(accountReqVo.getAccount());
        if (!commonMemberService.updateById(commonMemberEntity)) {
            return R.fail("设置失败");
        }

        return R.success();
    }
}
