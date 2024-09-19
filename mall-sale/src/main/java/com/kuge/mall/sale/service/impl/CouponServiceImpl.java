package com.kuge.mall.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.CouponRangeEnum;
import com.kuge.mall.common.constant.MemberCouponStatusEnum;
import com.kuge.mall.common.entity.CommonCouponEntity;
import com.kuge.mall.common.entity.CommonMemberCouponEntity;
import com.kuge.mall.common.service.CommonMemberCouponService;
import com.kuge.mall.common.service.CommonCouponService;
import com.kuge.mall.common.utils.*;
import com.kuge.mall.sale.service.CouponService;
import com.kuge.mall.sale.vo.CouponPageItemVo;
import com.kuge.mall.sale.vo.CouponPageReqVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/31
 */
@Service("couponService")
public class CouponServiceImpl implements CouponService {

    @Resource
    private CommonCouponService commonCouponService;

    @Resource
    private CommonMemberCouponService commonMemberCouponService;

    @Override
    public R<PageData<CouponPageItemVo>> pageList(CouponPageReqVo orderPageReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        List<CouponPageItemVo> couponPageItemVos = new ArrayList<>();

        List<CommonCouponEntity> commonCouponEntities = commonCouponService.list(new QueryWrapper<CommonCouponEntity>().eq("member_range", CouponRangeEnum.MEMBER_ALL.getCode()));
        if (!CollectionUtils.isEmpty(commonCouponEntities)) {
            couponPageItemVos.addAll(
                commonCouponEntities.stream().map(commonCouponEntity -> {
                    CouponPageItemVo couponPageItemVo = new CouponPageItemVo();
                    BeanUtils.copyProperties(commonCouponEntity, couponPageItemVo);
                    couponPageItemVo.setTypeDesc(CouponUtils.getTypeDesc(commonCouponEntity.getType()));
                    return couponPageItemVo;
                }).collect(Collectors.toList())
            );
        }


        List<CommonMemberCouponEntity> commonCouponMemberRelEntities = commonMemberCouponService.list(
            new QueryWrapper<CommonMemberCouponEntity>().eq("member_id", tokenDto.getId())
        );
        if (!CollectionUtils.isEmpty(commonCouponMemberRelEntities)) {
            couponPageItemVos.addAll(
                commonCouponMemberRelEntities.stream().map(commonCouponMemberEntity -> {
                    CommonCouponEntity commonCouponEntity = commonCouponService.getById(commonCouponMemberEntity.getCouponId());

                    boolean isNull = false;
                    if (StringUtils.hasLength(orderPageReqVo.getStatus())) {
                        if (Objects.equals(orderPageReqVo.getStatus(), MemberCouponStatusEnum.EXPIRED.getCode())) {
                            isNull = LocalDateTime.now().isBefore(commonCouponEntity.getEndTime());
                        } else {
                            isNull = !Objects.equals(orderPageReqVo.getStatus(), commonCouponMemberEntity.getStatus());
                        }
                    }

                    if (isNull) {
                        return null;
                    }

                    CouponPageItemVo couponPageItemVo = new CouponPageItemVo();
                    BeanUtils.copyProperties(commonCouponEntity, couponPageItemVo);
                    couponPageItemVo.setTypeDesc(CouponUtils.getTypeDesc(commonCouponEntity.getType()));
                    return couponPageItemVo;
                }).filter(Objects::nonNull).collect(Collectors.toList())
            );
        }

        PageData<CouponPageItemVo> pageData = PageData.getPageData(orderPageReqVo.getPageNum(), orderPageReqVo.getPageSize(), couponPageItemVos);

        return R.success(pageData);
    }
}
