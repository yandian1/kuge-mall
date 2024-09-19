package com.kuge.mall.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuge.mall.admin.service.SpuService;
import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.*;
import com.kuge.mall.common.mapper.CommonSkuMapper;
import com.kuge.mall.common.mapper.CommonSpuAttrMapper;
import com.kuge.mall.common.mapper.CommonSpuMapper;
import com.kuge.mall.common.service.*;
import com.kuge.mall.common.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-05-16 23:53:48
 */
@Service("spuService")
public class SpuServiceImpl implements SpuService {

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private CommonSkuService commonSkuService;

    @Resource
    private CommonCategoryService commonCategoryService;

    @Resource
    private CommonSpuAttrService commonSpuAttrService;

    @Resource
    private CommonBrandService commonBrandService;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private Executor threadPoolExecutor;


    @Transactional
    @Override
    public R<Void> add(SpuAddVo spuAddVo) {
        List<SpuAddVo.Sku> skus = spuAddVo.getSkus();
        // skus 为空
        if (CollectionUtils.isEmpty(skus)) {
            return R.fail("属性商品为空");
        }

        // 验证 attrs
        List<SpuAddVo.Attr> attrs = spuAddVo.getAttrs();
        verifyAttrs(attrs);

        // 添加 spu
        CommonSpuEntity spuEntity = new CommonSpuEntity();
        BeanUtils.copyProperties(spuAddVo, spuEntity);
        spuEntity.setStatus(0); // 新增商品默认为未上架

        if (!CollectionUtils.isEmpty(spuAddVo.getImgList())) {
            spuEntity.setImgs(String.join(",", spuAddVo.getImgList()));
        }
        if (!CollectionUtils.isEmpty(spuAddVo.getServiceIds())) {
            spuEntity.setService(String.join(",", spuAddVo.getServiceIds().stream().map(i -> i + "").collect(Collectors.toList())));
        }
        if (!commonSpuService.save(spuEntity)) {
            throw new CustomException("添加商品失败");
        }


        for (SpuAddVo.Attr attr : attrs) {
            List<SpuAddVo.Attr> children = attr.getChildren();
            // 添加父级 attr
            CommonSpuAttrEntity spuAttrEntity = new CommonSpuAttrEntity();

            spuAttrEntity.setSpuId(spuEntity.getId());
            spuAttrEntity.setName(attr.getName());

            if (!commonSpuAttrService.save(spuAttrEntity)) {
                throw new CustomException("添加属性失败");
            }

            // 添加子级 attr
            if (!CollectionUtils.isEmpty(children)) {
                List<CommonSpuAttrEntity> spuAttrEntities = new ArrayList<>();

                for (SpuAddVo.Attr attrChild : children) {
                    CommonSpuAttrEntity spuAttrEntityChild = new CommonSpuAttrEntity();

                    spuAttrEntityChild.setSpuId(spuEntity.getId());
                    spuAttrEntityChild.setPid(spuAttrEntity.getId());
                    spuAttrEntityChild.setName(attrChild.getName());

                    spuAttrEntities.add(spuAttrEntityChild);
                }

                if (!commonSpuAttrService.saveBatch(spuAttrEntities)) {
                    throw new CustomException("添加属性失败");
                }
            }
        }


        List<CommonSkuEntity> skuEntities = skus.stream().map(sku -> {
            CommonSkuEntity skuEntity = new CommonSkuEntity();

            BeanUtils.copyProperties(sku, skuEntity);
            skuEntity.setSpuId(spuEntity.getId());
            skuEntity.setShopId(spuAddVo.getShopId());
            skuEntity.setName(spuEntity.getName());
            skuEntity.setImg(spuEntity.getFirstImg());
            skuEntity.setWeight(sku.getWeight());

            return skuEntity;
        }).collect(Collectors.toList());

        if (!commonSkuService.saveBatch(skuEntities)) {
            throw new CustomException("添加商品失败");
        }

        return R.success("添加商品成功");
    }

    // @Transactional
    @Override
    public R<Void> updateSpu(SpuUpdateVo spuUpdateVo) {
        CommonSpuEntity commonSpuEntity = commonSpuService.getById(spuUpdateVo.getId());
        if (commonSpuEntity == null) {
            return R.fail("商品不存在");
        }

        List<SpuUpdateVo.Sku> newSkus = spuUpdateVo.getSkus();
        // skus 为空
        if (CollectionUtils.isEmpty(newSkus)) {
            return R.fail("属性商品为空");
        }

        // 验证 attrs
        List<SpuUpdateVo.Attr> pAttrs = spuUpdateVo.getAttrs();
        verifyAttrs(pAttrs);

        BeanUtils.copyProperties(spuUpdateVo, commonSpuEntity);

        if (!CollectionUtils.isEmpty(spuUpdateVo.getImgList())) {
            commonSpuEntity.setImgs(String.join(",", spuUpdateVo.getImgList()));
        }
        if (!CollectionUtils.isEmpty(spuUpdateVo.getServiceIds())) {
            commonSpuEntity.setService(String.join(",", spuUpdateVo.getServiceIds().stream().map(i -> i + "").collect(Collectors.toList())));
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();

        try {
            connection.setAutoCommit(false);
            CommonSpuMapper commonSpuMapper = sqlSession.getMapper(CommonSpuMapper.class);
            CommonSpuAttrMapper commonSpuAttrMapper = sqlSession.getMapper(CommonSpuAttrMapper.class);
            CommonSkuMapper commonSkuMapper = sqlSession.getMapper(CommonSkuMapper.class);


            // 更新 base
            CompletableFuture<Void> baseFuture = CompletableFuture.runAsync(() -> {
                if (commonSpuMapper.updateById(commonSpuEntity) < 1) {
                    throw new CustomException("更新商品失败");
                }
            }, threadPoolExecutor);

            // 更新 attr
            CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {
                List<CommonSpuAttrEntity> pSpuAttrEntities = commonSpuAttrMapper.selectList(new QueryWrapper<CommonSpuAttrEntity>().eq("spu_id", spuUpdateVo.getId()).isNull("pid"));
                for (CommonSpuAttrEntity pSpuAttrEntity : pSpuAttrEntities) {
                    if (pAttrs.stream().noneMatch(pAttr -> Objects.equals(pAttr.getName(), pSpuAttrEntity.getName()))) {
                        if (commonSpuAttrMapper.deleteById(pSpuAttrEntity.getId()) < 1 | commonSpuAttrMapper.delete(new QueryWrapper<CommonSpuAttrEntity>().eq("pid", pSpuAttrEntity.getId())) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    }
                }

                for (SpuUpdateVo.Attr pAttr : pAttrs) {
                    CommonSpuAttrEntity pTargetEntity = pSpuAttrEntities.stream().filter(pSpuAttrEntity -> Objects.equals(pSpuAttrEntity.getName(), pAttr.getName())).findAny().orElse(null);

                    // 旧属性中不存在和当前属性相等的属性，插入当前属性
                    if (pTargetEntity == null) {
                        // 插入
                        CommonSpuAttrEntity pSpuAttrEntity = new CommonSpuAttrEntity();
                        pSpuAttrEntity.setSpuId(spuUpdateVo.getId());
                        pSpuAttrEntity.setName(pAttr.getName());
                        if (commonSpuAttrMapper.insert(pSpuAttrEntity) < 1) {
                            throw new CustomException("更新商品失败");
                        }

                        List<SpuUpdateVo.Attr> children = pAttr.getChildren();
                        if (!CollectionUtils.isEmpty(children)) {
                            for (SpuUpdateVo.Attr child : children) {
                                CommonSpuAttrEntity cSpuAttrEntity = new CommonSpuAttrEntity();
                                cSpuAttrEntity.setPid(pSpuAttrEntity.getId());
                                cSpuAttrEntity.setSpuId(spuUpdateVo.getId());
                                cSpuAttrEntity.setName(child.getName());
                                if (commonSpuAttrMapper.insert(cSpuAttrEntity) < 1) {
                                    throw new CustomException("更新商品失败");
                                }
                            }
                        }
                    } else {
                        // 旧属性中存在和当前属性相等的属性，更新当前属性
                        List<CommonSpuAttrEntity> cSpuAttrEntities = commonSpuAttrMapper.selectList(new QueryWrapper<CommonSpuAttrEntity>().eq("pid", pTargetEntity.getId()));
                        List<SpuUpdateVo.Attr> cAttrs = pAttr.getChildren();

                        for (CommonSpuAttrEntity cSpuAttrEntity : cSpuAttrEntities) {
                            if (cAttrs.stream().noneMatch(cAttr -> Objects.equals(cAttr.getName(), cSpuAttrEntity.getName()))) {
                                if (commonSpuAttrMapper.deleteById(cSpuAttrEntity.getId()) < 1) {
                                    throw new CustomException("更新商品失败");
                                    // return R.fail("更新商品失败");
                                }
                            }
                        }

                        for (SpuUpdateVo.Attr cAttr : cAttrs) {
                            CommonSpuAttrEntity cTargetEntity = cSpuAttrEntities.stream().filter(cSpuAttrEntity -> Objects.equals(cSpuAttrEntity.getName(), cAttr.getName())).findAny().orElse(null);
                            if (cTargetEntity == null) {
                                CommonSpuAttrEntity cSpuAttrEntity = new CommonSpuAttrEntity();
                                cSpuAttrEntity.setPid(pTargetEntity.getId());
                                cSpuAttrEntity.setSpuId(spuUpdateVo.getId());
                                cSpuAttrEntity.setName(cAttr.getName());
                                if (commonSpuAttrMapper.insert(cSpuAttrEntity) < 1) {
                                    throw new CustomException("更新商品失败");
                                    // return R.fail("更新商品失败");
                                }
                            } else {
                                // 更新
                            }
                        }
                    }
                }
            }, threadPoolExecutor);

            // 更新 skus
            CompletableFuture<Void> skuFuture = CompletableFuture.runAsync(() -> {
                QueryWrapper<CommonSkuEntity> skuQuery = new QueryWrapper<CommonSkuEntity>().eq("spu_id", spuUpdateVo.getId());

                // 属性商品为空，直接添加
                List<CommonSkuEntity> commonSkuEntities = commonSkuMapper.selectList(skuQuery);
                if (CollectionUtils.isEmpty(commonSkuEntities)) {
                    for (SpuAddVo.Sku newSku : newSkus) {
                        CommonSkuEntity commonSkuEntity = new CommonSkuEntity();

                        BeanUtils.copyProperties(newSku, commonSkuEntity);
                        commonSkuEntity.setSpuId(spuUpdateVo.getId());
                        commonSkuEntity.setShopId(spuUpdateVo.getShopId());
                        commonSkuEntity.setName(commonSpuEntity.getName());
                        commonSkuEntity.setImg(commonSpuEntity.getFirstImg());

                        if (commonSkuMapper.insert(commonSkuEntity) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    }

                    return;
                }

                // 属性种类不一致，则先删除再添加sku
                Set<String> newKeys = JSON.parseObject(newSkus.get(0).getAttrs(), new TypeReference<Map<String, String>>() {}).keySet();
                Set<String> oldKeys = JSON.parseObject(commonSkuEntities.get(0).getAttrs(), new TypeReference<Map<String, String>>() {}).keySet();
                if (!newKeys.stream().sorted().collect(Collectors.joining()).equals(oldKeys.stream().sorted().collect(Collectors.joining()))) {
                    // 先删除
                    if (commonSkuMapper.delete(skuQuery) < 1) {
                        throw new CustomException("更新商品失败");
                    }

                    // 后添加
                    for (SpuAddVo.Sku newSku : newSkus) {
                        CommonSkuEntity commonSkuEntity = new CommonSkuEntity();

                        BeanUtils.copyProperties(newSku, commonSkuEntity);
                        commonSkuEntity.setSpuId(spuUpdateVo.getId());
                        commonSkuEntity.setShopId(spuUpdateVo.getShopId());
                        commonSkuEntity.setName(commonSpuEntity.getName());
                        commonSkuEntity.setImg(commonSpuEntity.getFirstImg());

                        if (commonSkuMapper.insert(commonSkuEntity) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    }

                    return;
                }

                // 属性种类一致：更新
                for (CommonSkuEntity commonSkuEntity : commonSkuEntities) {
                    // 如果旧属性商品在新商品中找不到对应，就进行删除
                    if (
                        newSkus.stream().noneMatch(newSku -> {
                            Map<String, String> newAttrs = JSON.parseObject(newSku.getAttrs(), new TypeReference<Map<String, String>>() {
                            });
                            Map<String, String> oldAttrs = JSON.parseObject(commonSkuEntity.getAttrs(), new TypeReference<Map<String, String>>() {
                            });

                            for (String key : oldAttrs.keySet()) {
                                if (!Objects.equals(newAttrs.get(key), oldAttrs.get(key))) {
                                    return false;
                                }
                            }

                            return true;
                        })
                    ) {
                        if (commonSkuMapper.deleteById(commonSkuEntity.getId()) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    }
                }

                for (SpuUpdateVo.Sku newSku : newSkus) {
                    CommonSkuEntity targetSkuEntity = commonSkuEntities.stream().filter(commonSkuEntity -> {
                        Map<String, String> newAttrs = JSON.parseObject(newSku.getAttrs(), new TypeReference<Map<String, String>>() {
                        });
                        Map<String, String> oldAttrs = JSON.parseObject(commonSkuEntity.getAttrs(), new TypeReference<Map<String, String>>() {
                        });

                        for (String key : oldAttrs.keySet()) {
                            if (!Objects.equals(newAttrs.get(key), oldAttrs.get(key))) {
                                return false;
                            }
                        }

                        return true;
                    }).findAny().orElse(null);

                    if (targetSkuEntity == null) {
                        CommonSkuEntity commonSkuEntity = new CommonSkuEntity();
                        BeanUtils.copyProperties(newSku, commonSkuEntity);
                        commonSkuEntity.setSpuId(spuUpdateVo.getId());
                        commonSkuEntity.setShopId(spuUpdateVo.getShopId());
                        commonSkuEntity.setName(commonSpuEntity.getName());
                        commonSkuEntity.setImg(commonSpuEntity.getFirstImg());

                        if (commonSkuMapper.insert(commonSkuEntity) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    } else {
                        // 更新
                        CommonSkuEntity commonSkuEntity = new CommonSkuEntity();
                        commonSkuEntity.setId(targetSkuEntity.getId());
                        BeanUtils.copyProperties(newSku, commonSkuEntity);
                        commonSkuEntity.setSpuId(spuUpdateVo.getId());
                        commonSkuEntity.setShopId(spuUpdateVo.getShopId());
                        commonSkuEntity.setName(commonSpuEntity.getName());
                        commonSkuEntity.setImg(commonSpuEntity.getFirstImg());
                        if (commonSkuMapper.updateById(commonSkuEntity) < 1) {
                            throw new CustomException("更新商品失败");
                        }
                    }
                }
            }, threadPoolExecutor);

            CompletableFuture.allOf(baseFuture, attrFuture, skuFuture).get();

            connection.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
            throw new RuntimeException(exception);
        }


        return R.success("更新商品成功");
    }

    @Override
    public R<SpuDetailVo> getSpu(Long id) {
        CommonSpuEntity spuEntity = commonSpuService.getById(id);
        if (spuEntity == null) {
            throw new CustomException("商品不存在");
        }

        SpuDetailVo spuDetailVo = new SpuDetailVo();

        CompletableFuture<Void> baseFuture = CompletableFuture.runAsync(() -> {
            // Monitor.printNow("baseFuture开始", "yyyy-MM-dd HH:mm:ss:SSS");
            calcBase(spuDetailVo, spuEntity);
            // Monitor.printNow("baseFuture结束", "yyyy-MM-dd HH:mm:ss:SSS");
        });

        CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {
            // Monitor.printNow("attrFuture开始", "yyyy-MM-dd HH:mm:ss:SSS");
            calcAttr(spuDetailVo, id);
            // Monitor.printNow("attrFuture结束", "yyyy-MM-dd HH:mm:ss:SSS");
        });

        CompletableFuture<Void> skuFuture = CompletableFuture.runAsync(() -> {
            // Monitor.printNow("skuFuture开始", "yyyy-MM-dd HH:mm:ss:SSS");
            calcSku(spuDetailVo, id);
            // Monitor.printNow("skuFuture结束", "yyyy-MM-dd HH:mm:ss:SSS");
        });

        CompletableFuture.allOf(baseFuture, attrFuture, skuFuture).join();

        return R.success(spuDetailVo);
    }

    private void genCategoryChain(Long id, List<Long> chain, List<CommonCategoryEntity> commonCategoryEntities) {
        Long pid = getParentCategoryId(id, commonCategoryEntities);

        // 父id为空 到根节点 结束
        // 父id不为空 未到根节点 继续
        if (pid != null) {
            chain.add(0, pid);
            genCategoryChain(pid, chain, commonCategoryEntities);
        }
    }

    private Long getParentCategoryId(Long id, List<CommonCategoryEntity> commonCategoryEntities) {
        for (CommonCategoryEntity commonCategoryEntity : commonCategoryEntities) {
            if (Objects.equals(commonCategoryEntity.getId(), id)) {
                return commonCategoryEntity.getPid();
            }
        }

        return null;
    }

    @Override
    public R<PageData<SpuPageResVo>> pageList(SpuPageReqVo spuPageReqVo) {
        QueryWrapper<CommonSpuEntity> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("create_time");
        if (StringUtils.hasLength(spuPageReqVo.getName())) {
            wrapper.like("name", spuPageReqVo.getName());
        }
        if (spuPageReqVo.getBrandId() != null) {
            wrapper.eq("brand_id", spuPageReqVo.getBrandId());
        }
        if (spuPageReqVo.getCategoryId() != null) {
            wrapper.eq("category_id", spuPageReqVo.getCategoryId());
        }
        if (spuPageReqVo.getStatus() != null) {
            wrapper.eq("status", spuPageReqVo.getStatus());
        }

        Page<CommonSpuEntity> res = commonSpuService.page(new Page<>(spuPageReqVo.getPageNum(), spuPageReqVo.getPageSize()), wrapper);
        PageData<SpuPageResVo> pageData = PageData.getPageData(res.getCurrent(), res.getSize(), res.getTotal(), res.getRecords().stream().map(record -> {
            SpuPageResVo spuPageResVo = new SpuPageResVo();
            BeanUtils.copyProperties(record, spuPageResVo);
            Long brandId = record.getBrandId();
            CommonBrandEntity commonBrandEntity = commonBrandService.getById(brandId);
            if (commonBrandEntity != null) {
                spuPageResVo.setBrandName(commonBrandEntity.getName());
            }
            Long categoryId = record.getCategoryId();
            CommonCategoryEntity commonCategoryEntity = commonCategoryService.getById(categoryId);
            if (commonCategoryEntity != null) {
                spuPageResVo.setCategoryName(commonCategoryEntity.getName());
            }
            spuPageResVo.setStatusDesc(GoodsUtils.getDesc(record.getStatus()));
            return spuPageResVo;
        }).collect(Collectors.toList()));

        return R.success(pageData);
    }

    @Override
    public R<Void> changeStatus(UpdateSpuStatusReqVo updateSpuStatusReqVo) {
        CommonSpuEntity commonSpuEntity = commonSpuService.getById(updateSpuStatusReqVo.getId());
        if (commonSpuEntity == null) {
            return R.fail("商品不存在");
        }


        BeanUtils.copyProperties(updateSpuStatusReqVo, commonSpuEntity);

        if (!commonSpuService.updateById(commonSpuEntity)) {
            return R.fail("更新状态失败");
        }

        return R.success("更新状态成功");
    }

    @Override
    public R<Void> del(Long id) {
        // spu不存在
        if (!commonSpuService.exists(new QueryWrapper<CommonSpuEntity>().eq("id", id))) {
            return R.fail("商品不存在");
        }

        // 删除spu
        if (!commonSpuService.removeById(id)) {
            return R.fail("删除商品失败");
        }


        // 删除attr
        QueryWrapper<CommonSpuAttrEntity> attrWrapper = new QueryWrapper<CommonSpuAttrEntity>().eq("spu_id", id);
        if (commonSpuAttrService.exists(attrWrapper)) {
            if (!commonSpuAttrService.remove(attrWrapper)) {
                throw new CustomException("删除商品失败");
            }
        }

        // 删除sku
        QueryWrapper<CommonSkuEntity> skuWrapper = new QueryWrapper<CommonSkuEntity>().eq("spu_id", id);
        if (commonSkuService.exists(skuWrapper)) {
            if (!commonSkuService.remove(skuWrapper)) {
                throw new CustomException("删除商品失败");
            }
        }


        return R.success("删除商品成功");
    }

    private <T> void verifyAttrs(List<T> attrs) {
        if (CollectionUtils.isEmpty(attrs)) {
            throw new CustomException("商品属性不能为空");
        }

        if (listRepeat(attrs)) {
            throw new CustomException("商品属性不能重复");
        };

        Class<?> aClass = attrs.get(0).getClass();
        for (T attr : attrs) {
            try {
                Method getChildren = aClass.getMethod("getChildren");
                List<T> children = (List<T>) getChildren.invoke(attr);
                if (CollectionUtils.isEmpty(children)) {
                    throw new CustomException("商品属性值不能为空");
                }
                if (listRepeat(children)) {
                    throw new CustomException("商品属性值不能重复");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> boolean listRepeat(List<T> attrs) {
        Class<?> aClass = attrs.get(0).getClass();

        List<String> nameList = attrs.stream().map(attr -> {
            try {
                Method getName = aClass.getMethod("getName");
                return (String) getName.invoke(attr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        HashSet<String> nameSet = new HashSet<>(nameList);

        return nameList.size() != nameSet.size();
    }

    private void calcBase(SpuDetailVo spuDetailVo, CommonSpuEntity spuEntity) {
        BeanUtils.copyProperties(spuEntity, spuDetailVo);

        String service = spuEntity.getService();
        if (StringUtils.hasLength(service)) {
            List<String> list = Arrays.asList(service.split(","));
            if (!CollectionUtils.isEmpty(list)) {
                spuDetailVo.setServiceIds(list.stream().map(i -> Long.valueOf(i)).collect(Collectors.toList()));
            }
        }

        // 轮播图
        String imgs = spuEntity.getImgs();
        if (StringUtils.hasLength(imgs)) {
            spuDetailVo.setImgList(Arrays.asList(imgs.split(",")));
        }

        // 分类id
        List<CommonCategoryEntity> commonCategoryEntities = commonCategoryService.list();
        List<Long> categoryIds = new ArrayList<>(Arrays.asList(spuEntity.getCategoryId()));
        genCategoryChain(spuEntity.getCategoryId(), categoryIds, commonCategoryEntities);
        spuDetailVo.setCategoryIds(categoryIds);
    }

    private void calcAttr(SpuDetailVo spuDetailVo, Long spuId) {
        // attr
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
                        if (Objects.equals(childAttrEntity.getPid(), spuAttrEntity.getId())) {
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

    private void calcSku(SpuDetailVo spuDetailVo, Long spuId) {
        List<CommonSkuEntity> skuEntities = commonSkuService.list(new QueryWrapper<CommonSkuEntity>().eq("spu_id", spuId));
        if (!CollectionUtils.isEmpty(skuEntities)) {
            spuDetailVo.setSkus(skuEntities.stream().map(skuEntity -> {
                SpuDetailVo.Sku sku = new SpuDetailVo.Sku();
                BeanUtils.copyProperties(skuEntity, sku);
                return sku;
            }).collect(Collectors.toList()));
        }
    }

    private void throwError() {
        throw new CustomException("XXXXX战警");
    }
}