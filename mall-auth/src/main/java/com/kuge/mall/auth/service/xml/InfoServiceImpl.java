package com.kuge.mall.auth.service.xml;

import com.kuge.mall.auth.service.InfoService;
import com.kuge.mall.auth.utils.UserUtils;
import com.kuge.mall.auth.vo.AdminInfoVo;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.service.CommonUserService;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.R;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/23
 */
@Service("infoService")
public class InfoServiceImpl implements InfoService {

    @Resource
    private CommonUserService commonUserService;
    
    @Resource
    private UserUtils userUtils;

    @Override
    public R<AdminInfoVo> adminInfo(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        CommonUserEntity commonUserEntity = commonUserService.getById(tokenDto.getId());

        AdminInfoVo adminInfoVo = userUtils.genAdminInfo(commonUserEntity);
        String token = JwtUtils.getToken(request);
        adminInfoVo.setToken(token);

        return R.success(adminInfoVo);
    }
}
