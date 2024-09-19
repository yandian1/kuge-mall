package com.kuge.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.MemberCouponStatusEnum;
import com.kuge.mall.common.entity.CommonCouponEntity;
import com.kuge.mall.common.entity.CommonMemberCouponEntity;
import com.kuge.mall.common.service.CommonCouponService;
import com.kuge.mall.common.service.CommonMemberCouponService;
import com.kuge.mall.common.utils.CouponUtils;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.service.MemberCouponService;
import com.kuge.mall.member.vo.MemberCouponPageItemVo;
import com.kuge.mall.member.vo.MemberCouponPageReqVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/23
 */
@Service("memberCouponService")
public class MemberCouponServiceImpl implements MemberCouponService {

    @Resource
    private CommonMemberCouponService commonMemberCouponService;

    @Resource
    private CommonCouponService commonCouponService;

    @Override
    public R<PageData<MemberCouponPageItemVo>> pageList(MemberCouponPageReqVo memberCouponPageReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        List<CommonMemberCouponEntity> commonMemberCouponEntities = commonMemberCouponService.list(new QueryWrapper<CommonMemberCouponEntity>().eq("member_id", tokenDto.getId()));
        if (CollectionUtils.isEmpty(commonMemberCouponEntities)) {
            return R.success(PageData.emptyPageData(MemberCouponPageItemVo.class));
        }

        return R.success(
            PageData.getPageData(
                memberCouponPageReqVo.getPageNum(),
                memberCouponPageReqVo.getPageSize(),
                commonMemberCouponEntities.stream().map(commonMemberCouponEntity -> {
                    CommonCouponEntity commonCouponEntity = commonCouponService.getById(commonMemberCouponEntity.getCouponId());
                    LocalDateTime startTime = commonCouponEntity.getStartTime();
                    LocalDateTime endTime = commonCouponEntity.getEndTime();
                    LocalDateTime now = LocalDateTime.now();
                    boolean isNull = true;

                    // 未使用
                    if (Objects.equals(memberCouponPageReqVo.getStatus(), MemberCouponStatusEnum.UNUSE.getCode())) {
                        if (
                            (now.isEqual(startTime) || now.isAfter(startTime))
                                && (now.isEqual(endTime) || now.isBefore(endTime))
                                && Objects.equals(commonMemberCouponEntity.getStatus(), MemberCouponStatusEnum.UNUSE.getCode())
                        ) {
                            isNull = false;
                        }
                    }

                    // 已使用
                    if (Objects.equals(memberCouponPageReqVo.getStatus(), MemberCouponStatusEnum.USED.getCode())) {
                        if (Objects.equals(commonMemberCouponEntity.getStatus(), MemberCouponStatusEnum.USED.getCode())) {
                            isNull = false;
                        }
                    }

                    // 已过期
                    if (Objects.equals(memberCouponPageReqVo.getStatus(), MemberCouponStatusEnum.EXPIRED.getCode())) {
                        if (
                            now.isAfter(endTime)
                                && !Objects.equals(commonMemberCouponEntity.getStatus(), MemberCouponStatusEnum.USED.getCode())
                        ) {
                            isNull = false;
                        }
                    }

                    if (isNull) {
                        return null;
                    }


                    MemberCouponPageItemVo memberCouponPageItemVo = new MemberCouponPageItemVo();
                    BeanUtils.copyProperties(commonCouponEntity, memberCouponPageItemVo);
                    memberCouponPageItemVo.setTypeDesc(CouponUtils.getTypeDesc(commonCouponEntity.getType()));
                    memberCouponPageItemVo.setCouponId(commonMemberCouponEntity.getCouponId());
                    memberCouponPageItemVo.setMemberCouponId(commonMemberCouponEntity.getId());
                    return memberCouponPageItemVo;

                }).filter(Objects::nonNull).collect(Collectors.toList())
            )
        );
    }
}
