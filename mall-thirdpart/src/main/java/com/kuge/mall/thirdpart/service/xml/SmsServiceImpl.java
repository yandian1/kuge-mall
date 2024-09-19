package com.kuge.mall.thirdpart.service.xml;

import com.kuge.mall.common.constant.SmsConstant;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.thirdpart.service.SmsService;
import com.kuge.mall.thirdpart.utils.SmsUtils;
import com.kuge.mall.thirdpart.vo.SmsCodeReqVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/23
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SmsUtils smsUtils;

    @Override
    public R<Void> sendCode(SmsCodeReqVo smsCodeReqVo) {

        // 生成验证码
        String code = smsUtils.genVerifyCode();

        // 发送短信验证码
        smsUtils.sendVerifyCode(smsCodeReqVo.getAccount(), code);

        // 存入 redis
        stringRedisTemplate.opsForValue().set(SmsConstant.REDIS_VERIFY_CODE_PREFIX + smsCodeReqVo.getAccount(), code);

        return R.success();
    }
}
