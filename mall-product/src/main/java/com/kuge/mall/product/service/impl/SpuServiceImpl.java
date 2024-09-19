package com.kuge.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.CouponRangeEnum;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.service.SpuService;
import com.kuge.mall.product.vo.SpuDetailVo;
import com.kuge.mall.product.vo.SpuPageReqVo;
import com.kuge.mall.product.vo.SpuPageResVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@Service("spuService")
public class SpuServiceImpl implements SpuService {

    @Resource
    private CommonSpuService commonSpuService;
    
    @Resource
    private CommonSpuAttrService commonSpuAttrService;

    @Resource
    private CommonSkuService commonSkuService;

    @Resource
    private CommonShopService commonShopService;

    @Resource
    private CommonDictValueService commonDictValueService;

    @Resource
    private CommonCouponSpuService commonCouponSpuService;

    @Resource
    private CommonMemberCouponService commonMemberCouponService;

    @Resource
    private CommonCouponService commonCouponService;

    @Override
    public R<PageData<SpuPageResVo>> pageList(SpuPageReqVo spuPageReqVo, HttpServletRequest request) {
        QueryWrapper<CommonSpuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (StringUtils.hasLength(spuPageReqVo.getName())) {
            wrapper.like("name", spuPageReqVo.getName());
        }
        if (spuPageReqVo.getCategoryId() != null) {
            wrapper.like("category_id", spuPageReqVo.getCategoryId());
        }

        if (spuPageReqVo.getCouponId() == null) {
            return R.success(
                PageData.getPageData(
                    commonSpuService.page(new Page<>(spuPageReqVo.getPageNum(), spuPageReqVo.getPageSize()), wrapper), SpuPageResVo.class
                )
            );
        } else {
            CommonCouponEntity commonCouponEntity = commonCouponService.getById(spuPageReqVo.getCouponId());
            String memberRange = commonCouponEntity.getMemberRange();
            String goodsRange = commonCouponEntity.getGoodsRange();

            // 全部用户
            if (Objects.equals(memberRange, CouponRangeEnum.MEMBER_ALL.getCode())) {
                // 全部商品
                if (Objects.equals(goodsRange, CouponRangeEnum.GOODS_ALL.getCode())) {
                    // 对商品不做过滤
                    return R.success(
                        PageData.getPageData(
                            commonSpuService.page(new Page<>(spuPageReqVo.getPageNum(), spuPageReqVo.getPageSize()), wrapper), SpuPageResVo.class
                        )
                    );
                }
                // 指定商品
                if (Objects.equals(goodsRange, CouponRangeEnum.GOODS_SPECIFIC.getCode())) {
                    // 获取指定商品
                    List<CommonCouponSpuEntity> commonCouponSpuEntities = commonCouponSpuService.list(
                        new QueryWrapper<CommonCouponSpuEntity>()
                            .eq("coupon_id", spuPageReqVo.getCouponId())
                    );

                    if (CollectionUtils.isEmpty(commonCouponSpuEntities)) {
                        return R.success(PageData.emptyPageData(SpuPageResVo.class));
                    }

                    List<Long> spuIds = commonCouponSpuEntities.stream().map(CommonCouponSpuEntity::getSpuId).collect(Collectors.toList());

                    return R.success(
                        PageData.getPageData(
                            spuPageReqVo.getPageNum(),
                            spuPageReqVo.getPageSize(),
                            commonSpuService.listByIds(spuIds),
                            SpuPageResVo.class
                        )
                    );
                }
            }

            // 指定用户
            if (Objects.equals(memberRange, CouponRangeEnum.MEMBER_SPECIFIC.getCode())) {
                TokenDto tokenDto = JwtUtils.parseToken(request);
                // 非指定用户
                if (
                    !commonMemberCouponService.exists(new QueryWrapper<CommonMemberCouponEntity>().eq("member_id", tokenDto.getId()).eq("coupon_id", spuPageReqVo.getCouponId()))
                ) {
                    return R.success(PageData.emptyPageData(SpuPageResVo.class));
                }

                // 全部商品
                if (Objects.equals(goodsRange, CouponRangeEnum.GOODS_ALL.getCode())) {
                    // 对商品不做过滤
                    return R.success(
                        PageData.getPageData(
                            commonSpuService.page(new Page<>(spuPageReqVo.getPageNum(), spuPageReqVo.getPageSize()), wrapper), SpuPageResVo.class
                        )
                    );
                }
                // 指定商品
                if (Objects.equals(goodsRange, CouponRangeEnum.GOODS_SPECIFIC.getCode())) {

                    // 获取指定商品
                    List<CommonCouponSpuEntity> commonCouponSpuEntities = commonCouponSpuService.list(
                        new QueryWrapper<CommonCouponSpuEntity>()
                            .eq("coupon_id", spuPageReqVo.getCouponId())
                    );

                    if (CollectionUtils.isEmpty(commonCouponSpuEntities)) {
                        return R.success(PageData.emptyPageData(SpuPageResVo.class));
                    }

                    List<Long> spuIds = commonCouponSpuEntities.stream().map(CommonCouponSpuEntity::getSpuId).collect(Collectors.toList());

                    return R.success(
                        PageData.getPageData(
                            spuPageReqVo.getPageNum(),
                            spuPageReqVo.getPageSize(),
                            commonSpuService.listByIds(spuIds),
                            SpuPageResVo.class
                        )
                    );
                }
            }

            return R.success(PageData.emptyPageData(SpuPageResVo.class));
        }
    }

    @Override
    public R<SpuDetailVo> getSpu(Long id, HttpServletRequest request) {
        CommonSpuEntity spuEntity = commonSpuService.getById(id);
        if (spuEntity == null) {
            throw new CustomException("商品不存在");
        }

        if (spuEntity.getStatus() == 0) {
            throw new CustomException("商品已下架");
        }

        List<CommonSkuEntity> skuEntities = commonSkuService.list(new QueryWrapper<CommonSkuEntity>().eq("spu_id", id));
        if (!CollectionUtils.isEmpty(skuEntities) && skuEntities.stream().allMatch(skuEntity -> skuEntity.getStock() < 1)) {
            throw new CustomException("商品库存不足");
        }

        SpuDetailVo spuDetailVo = new SpuDetailVo();


        CompletableFuture<Void> baseFuture = CompletableFuture.runAsync(() -> {
            calcBase(spuDetailVo, spuEntity, id);
        });

        CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {
            calcAttrs(spuDetailVo, id);
        });

        CompletableFuture<Void> skuFuture = CompletableFuture.runAsync(() -> {
            calcSkus(spuDetailVo, id);
        });

        CompletableFuture.allOf(baseFuture, skuFuture, attrFuture).join();

        return R.success(spuDetailVo);
    }

    private void calcBase(SpuDetailVo spuDetailVo, CommonSpuEntity spuEntity, Long spuId) {
        // 基本信息
        BeanUtils.copyProperties(spuEntity, spuDetailVo);
        String imgs = spuEntity.getImgs();
        if (StringUtils.hasLength(imgs)) {
            spuDetailVo.setImgList(Arrays.asList(imgs.split(",")));
        }

        String service = spuEntity.getService();
        if (StringUtils.hasLength(service)) {
            List<String> serviceIds = Arrays.asList(service.split(","));
            List<CommonDictValueEntity> commonDictValueEntities = commonDictValueService.listByIds(serviceIds);
            spuDetailVo.setServices(commonDictValueEntities.stream().map(CommonDictValueEntity::getValue).collect(Collectors.toList()));
        }

        // 店铺信息
        Long shopId = spuEntity.getShopId();
        if (shopId != null) {
            CommonShopEntity commonShopEntity = commonShopService.getById(shopId);
            if (commonShopEntity != null) {
                spuDetailVo.setShopName(commonShopEntity.getName());
            }
        }
    }

    private void calcSkus(SpuDetailVo spuDetailVo, Long spuId) {
        List<CommonSkuEntity> skuEntities = commonSkuService.list(new QueryWrapper<CommonSkuEntity>().eq("spu_id", spuId));
        if (!CollectionUtils.isEmpty(skuEntities)) {
            spuDetailVo.setSkus(skuEntities.stream().map(skuEntity -> {
                SpuDetailVo.Sku sku = new SpuDetailVo.Sku();
                BeanUtils.copyProperties(skuEntity, sku);
                return sku;
            }).collect(Collectors.toList()));
        }
    }

    private void calcAttrs(SpuDetailVo spuDetailVo, Long spuId) {
        List<CommonSpuAttrEntity> spuAttrEntities = commonSpuAttrService.list(new QueryWrapper<CommonSpuAttrEntity>().eq("spu_id", spuId));
        if (!CollectionUtils.isEmpty(spuAttrEntities)) {
            ArrayList<SpuDetailVo.Attr> attrs = new ArrayList<>();

            for (CommonSpuAttrEntity spuAttrEntity : spuAttrEntities) {
                // 一级属性
                if (spuAttrEntity.getPid() == null) {
                    SpuDetailVo.Attr attr = new SpuDetailVo.Attr();

                    BeanUtils.copyProperties(spuAttrEntity, attr);
                    ArrayList<SpuDetailVo.Attr> children = new ArrayList<>();
                    attr.setChildren(children);

                    // 二级属性
                    for (CommonSpuAttrEntity childAttrEntity : spuAttrEntities) {
                        if (Objects.equals(childAttrEntity.getPid(), attr.getId())) {
                            SpuDetailVo.Attr childAttr = new SpuDetailVo.Attr();

                            BeanUtils.copyProperties(childAttrEntity, childAttr);

                            children.add(childAttr);
                        }
                    }


                    attrs.add(attr);
                }
            }

            spuDetailVo.setAttrs(attrs);
        }
    }
}