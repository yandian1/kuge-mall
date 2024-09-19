package com.kuge.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.CouponService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.constant.CouponRangeEnum;
import com.kuge.mall.common.constant.MemberCouponStatusEnum;
import com.kuge.mall.common.entity.CommonCouponEntity;
import com.kuge.mall.common.entity.CommonMemberCouponEntity;
import com.kuge.mall.common.entity.CommonCouponSpuEntity;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.GoodsUtils;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
@Service("couponService")
public class CouponServiceImpl implements CouponService {

    @Resource
    private CommonCouponService commonCouponService;

    @Resource
    private CommonCouponSpuService commonCouponSpuService;

    @Resource
    private CommonMemberCouponService commonMemberCouponService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private CommonMemberService commonMemberService;

    @Override
    public R<Void> add(CouponAddVo couponAddVo) {
        if (
            commonCouponService.exists(new QueryWrapper<CommonCouponEntity>().eq("name", couponAddVo.getName()))
        ) {
            return R.fail("优惠券名称重复");
        }

        // 优惠券基本信息
        CommonCouponEntity commonCouponEntity = new CommonCouponEntity();
        BeanUtils.copyProperties(couponAddVo, commonCouponEntity);

        if (!commonCouponService.save(commonCouponEntity)) {
            return R.fail("添加优惠券失败");
        }

        // 应用商品
        if (Objects.equals(couponAddVo.getGoodsRange(), CouponRangeEnum.GOODS_SPECIFIC.getCode())) {
            List<Long> spuIds = couponAddVo.getSpuIds();
            if (CollectionUtils.isEmpty(spuIds)) {
                new CustomException("请添加应用商品");
            }

            if (!commonCouponSpuService.saveBatch(
                spuIds.stream().map(spuId -> {
                    CommonCouponSpuEntity commonCouponSpuEntity = new CommonCouponSpuEntity();
                    commonCouponSpuEntity.setCouponId(commonCouponEntity.getId());
                    commonCouponSpuEntity.setSpuId(spuId);
                    return commonCouponSpuEntity;
                }).collect(Collectors.toList())
            )) {
                return R.fail("添加优惠券失败");
            }
        }

        // 应用会员
        List<Long> memberIds = new ArrayList<>();
        if (Objects.equals(couponAddVo.getMemberRange(), CouponRangeEnum.MEMBER_SPECIFIC.getCode())) {
            if (CollectionUtils.isEmpty(couponAddVo.getMemberIds())) {
                new CustomException("请添加应用会员");
            }
            memberIds = couponAddVo.getMemberIds();
        } else {
            List<CommonMemberEntity> commonMemberEntities = commonMemberService.list();
            if (!CollectionUtils.isEmpty(commonMemberEntities)) {
                memberIds = commonMemberEntities.stream().map(CommonMemberEntity::getId).collect(Collectors.toList());

            }
        }
        System.out.println("memberIds = " + memberIds);
        if (!commonMemberCouponService.saveBatch(
            memberIds.stream().map(memberId -> {
                CommonMemberCouponEntity commonMemberCouponEntity = new CommonMemberCouponEntity();
                commonMemberCouponEntity.setCouponId(commonCouponEntity.getId());
                commonMemberCouponEntity.setMemberId(memberId);
                commonMemberCouponEntity.setStatus(MemberCouponStatusEnum.UNUSE.getCode());
                return commonMemberCouponEntity;
            }).collect(Collectors.toList())
        )) {
            return R.fail("添加优惠券失败");
        }

        return R.success("添加优惠券成功");
    }

    @Override
    public R<Void> updateCoupon(CouponUpdateVo couponUpdateVo) {
        // 更新的优惠券不存在
        CommonCouponEntity commonCouponEntity = commonCouponService.getById(couponUpdateVo.getId());
        if (commonCouponEntity == null) {
            return R.fail("优惠券不存在");
        }

        if (
            commonCouponService.exists(
                new QueryWrapper<CommonCouponEntity>()
                    .ne("id", couponUpdateVo.getId())
                    .eq("name", couponUpdateVo.getName())
            )
        ) {
            return R.fail("优惠券名重复");
        }

        BeanUtils.copyProperties(couponUpdateVo, commonCouponEntity);

        if (!commonCouponService.updateById(commonCouponEntity)) {
            return R.fail("编辑优惠券失败");
        }

        // 应用商品
        commonCouponSpuService.remove(new QueryWrapper<CommonCouponSpuEntity>().eq("coupon_id", couponUpdateVo.getId()));
        if (Objects.equals(couponUpdateVo.getGoodsRange(), CouponRangeEnum.GOODS_SPECIFIC.getCode())) {
            List<Long> spuIds = couponUpdateVo.getSpuIds();
            if (CollectionUtils.isEmpty(spuIds)) {
                new CustomException("请添加应用商品");
            }

            if (!commonCouponSpuService.saveBatch(
                spuIds.stream().map(spuId -> {
                    CommonCouponSpuEntity commonCouponSpuEntity = new CommonCouponSpuEntity();
                    commonCouponSpuEntity.setCouponId(commonCouponEntity.getId());
                    commonCouponSpuEntity.setSpuId(spuId);
                    return commonCouponSpuEntity;
                }).collect(Collectors.toList())
            )) {
                return R.fail("添加优惠券失败");
            }
        }

        // 应用会员
        commonMemberCouponService.remove(new QueryWrapper<CommonMemberCouponEntity>().eq("coupon_id", couponUpdateVo.getId()));
        List<Long> memberIds = new ArrayList<>();
        if (Objects.equals(couponUpdateVo.getMemberRange(), CouponRangeEnum.MEMBER_SPECIFIC.getCode())) {
            if (CollectionUtils.isEmpty(couponUpdateVo.getMemberIds())) {
                new CustomException("请添加应用会员");
            }
            memberIds = couponUpdateVo.getMemberIds();
        } else {
            List<CommonMemberEntity> commonMemberEntities = commonMemberService.list();
            if (!CollectionUtils.isEmpty(commonMemberEntities)) {
                memberIds = commonMemberEntities.stream().map(CommonMemberEntity::getId).collect(Collectors.toList());

            }
        }

        if (!commonMemberCouponService.saveBatch(
            memberIds.stream().map(memberId -> {
                CommonMemberCouponEntity commonMemberCouponEntity = new CommonMemberCouponEntity();
                commonMemberCouponEntity.setCouponId(commonCouponEntity.getId());
                commonMemberCouponEntity.setMemberId(memberId);
                commonMemberCouponEntity.setStatus(MemberCouponStatusEnum.UNUSE.getCode());
                return commonMemberCouponEntity;
            }).collect(Collectors.toList())
        )) {
            return R.fail("添加优惠券失败");
        }


        return R.success("编辑优惠券成功");
    }

    @Override
    public R<Void> del(Long id) {
        // 优惠券不存在
        if (commonCouponService.getById(id) == null) {
            return R.fail("优惠券不存在");
        }

        // 删除优惠券
        if (!commonCouponService.removeById(id)) {
            return R.fail("删除优惠券失败");
        }

        // 应用商品
        commonCouponSpuService.remove(new QueryWrapper<CommonCouponSpuEntity>().eq("coupon_id", id));


        // 应用用户
        commonMemberCouponService.remove(new QueryWrapper<CommonMemberCouponEntity>().eq("coupon_id", id));

        return R.success("删除优惠券成功");
    }

    @Override
    public R<PageData<CouponPageItemVo>> pageList(CouponPageReqVo couponPageReqVo) {
        String name = couponPageReqVo.getName();
        String type = couponPageReqVo.getType();

        QueryWrapper<CommonCouponEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");

        if (StringUtils.hasLength(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.hasLength(type)) {
            wrapper.eq("type", type);
        }

        Page<CommonCouponEntity> res = commonCouponService.page(new Page<>(couponPageReqVo.getPageNum(), couponPageReqVo.getPageSize()), wrapper);
        PageData<CouponPageItemVo> pageData = PageData.getPageData(res, CouponPageItemVo.class);

        return R.success(pageData);
    }

    @Override
    public R<CouponDetailVo> getCoupon(Long id) {
        CommonCouponEntity commonCouponEntity = commonCouponService.getById(id);
        if (commonCouponEntity == null) {
            throw new CustomException("优惠券不存在");
        }

        CouponDetailVo couponDetailVo = new CouponDetailVo();
        BeanUtils.copyProperties(commonCouponEntity, couponDetailVo);

        // spu
        List<Long> spuIds = commonCouponSpuService.list(new QueryWrapper<CommonCouponSpuEntity>().eq("coupon_id", id)).stream().map(CommonCouponSpuEntity::getSpuId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(spuIds)) {
            couponDetailVo.setSpuList(
                commonSpuService.listByIds(spuIds).stream().map(commonSpuEntity -> {
                    CouponDetailVo.Spu spu = new CouponDetailVo.Spu();
                    BeanUtils.copyProperties(commonSpuEntity, spu);
                    spu.setStatusDesc(GoodsUtils.getDesc(commonSpuEntity.getStatus()));
                    return spu;
                }).collect(Collectors.toList())
            );
        }

        // member
        List<Long> memberIds = commonMemberCouponService.list(new QueryWrapper<CommonMemberCouponEntity>().eq("coupon_id", id)).stream().map(CommonMemberCouponEntity::getMemberId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(memberIds)) {
            couponDetailVo.setMemberList(
                commonMemberService.listByIds(memberIds).stream().map(commonMemberEntity -> {
                    CouponDetailVo.Member member = new CouponDetailVo.Member();
                    BeanUtils.copyProperties(commonMemberEntity, member);
                    return member;
                }).collect(Collectors.toList())
            );
        }

        return R.success(couponDetailVo);
    }
}