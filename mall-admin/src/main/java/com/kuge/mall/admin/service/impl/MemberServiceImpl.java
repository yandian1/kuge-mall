package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.MemberService;
import com.kuge.mall.admin.vo.MemberPageItemVo;
import com.kuge.mall.admin.vo.MemberPageReqVo;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.service.CommonMemberService;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * created by xbxie on 2024/5/23
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Resource
    private CommonMemberService commonMemberService;

    @Override
    public R<PageData<MemberPageItemVo>> pageList(MemberPageReqVo memberPageReqVo) {
        QueryWrapper<CommonMemberEntity> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(memberPageReqVo.getName())) {
            queryWrapper.like("name", memberPageReqVo.getName());
        }

        if (StringUtils.hasLength(memberPageReqVo.getAccount())) {
            queryWrapper.like("account", memberPageReqVo.getAccount());
        }

        Page<CommonMemberEntity> page = commonMemberService.page(new Page<>(memberPageReqVo.getPageNum(), memberPageReqVo.getPageSize()), queryWrapper);
        PageData<MemberPageItemVo> pageData = PageData.getPageData(page, MemberPageItemVo.class);

        return R.success(pageData);
    }
}
