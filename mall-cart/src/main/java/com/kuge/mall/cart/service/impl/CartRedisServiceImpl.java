package com.kuge.mall.cart.service.impl;

import com.kuge.mall.cart.service.CartRedisService;
import com.kuge.mall.cart.vo.CartAddVo;
import com.kuge.mall.cart.vo.CartInfoResVo;
import com.kuge.mall.cart.vo.ChangeQuantityReqVo;
import com.kuge.mall.cart.vo.ChangeSelectReqVo;
import com.kuge.mall.common.dto.CartSkuDto;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.RedisKeyConstant;
import com.kuge.mall.common.constant.SpuStatusEnum;
import com.kuge.mall.common.entity.CommonShopEntity;
import com.kuge.mall.common.entity.CommonSkuEntity;
import com.kuge.mall.common.entity.CommonSpuEntity;
import com.kuge.mall.common.service.CommonShopService;
import com.kuge.mall.common.service.CommonSkuService;
import com.kuge.mall.common.service.CommonSpuService;
import com.kuge.mall.common.utils.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024-06-03 12:31:38
 */
@Service("cartRedisService")
public class CartRedisServiceImpl implements CartRedisService {

    @Resource
    private CommonSkuService commonSkuService;

    @Resource
    private CommonSpuService commonSpuService;

    @Resource
    private CommonShopService commonShopService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public R<Void> add(CartAddVo cartAddVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        // 商品不存在
        CommonSkuEntity commonSkuEntity = commonSkuService.getById(cartAddVo.getSkuId());
        if (commonSkuEntity == null) {
            return R.fail("商品不存在");
        }

        CommonSpuEntity commonSpuEntity = commonSpuService.getById(commonSkuEntity.getSpuId());
        if (commonSpuEntity == null) {
            return R.fail("商品不存在");
        }

        // 商品已下架
        if (Objects.equals(commonSpuEntity.getStatus(), SpuStatusEnum.SHELF_DOWN.getCode())) {
            return R.fail("商品已下架");
        }

        CommonShopEntity commonShopEntity = commonShopService.getById(commonSkuEntity.getShopId());
        if (commonShopEntity == null) {
            return R.fail("店铺不存在");
        }

        String userKey = RedisKeyConstant.CART_PREFIX + tokenDto.getId();
        String shopHashKey = commonShopEntity.getId() + "";

        String shopKey = userKey + "_" + commonShopEntity.getId();
        String skuHashKey = commonSkuEntity.getId() + "";

        boolean canAddUserShop = Boolean.FALSE.equals(stringRedisTemplate.hasKey(userKey)) || Boolean.FALSE.equals(stringRedisTemplate.opsForHash().hasKey(userKey, shopHashKey));

        boolean canAddShopSku = Boolean.FALSE.equals(stringRedisTemplate.opsForHash().hasKey(shopKey, skuHashKey));

        Object goodsObj = stringRedisTemplate.opsForHash().get(shopKey, skuHashKey);

        SessionCallback<Object> callback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                if (canAddUserShop) {
                    // 库存不足
                    if (commonSkuEntity.getStock() < cartAddVo.getQuantity()) {
                        throw new CustomException("商品库存不足");
                    }

                    // 添加 user - shop
                    CartInfoResVo.ShopItem shopItem = new CartInfoResVo.ShopItem();
                    shopItem.setId(commonShopEntity.getId());
                    shopItem.setName(commonShopEntity.getName());
                    shopItem.setSelected(true);
                    shopItem.setIndeterminate(false);
                    stringRedisTemplate.opsForHash().put(userKey, shopHashKey, shopItem);

                    // 添加 shop - sku
                    CartSkuDto goods = new CartSkuDto();
                    goods.setSpuId(commonSkuEntity.getSpuId());
                    goods.setSkuId(commonSkuEntity.getId());
                    goods.setName(commonSkuEntity.getName());
                    goods.setAttrs(GoodsUtils.getAttrValues(commonSkuEntity.getAttrs()));
                    goods.setImg(commonSkuEntity.getImg());
                    goods.setNum(cartAddVo.getQuantity());
                    goods.setStock(commonSkuEntity.getStock());
                    goods.setPrice(commonSkuEntity.getPrice());
                    goods.setSelected(true);

                    stringRedisTemplate.opsForHash().put(shopKey, skuHashKey, goods);
                } else {
                    if (canAddShopSku) {
                        // 添加 shop - sku
                        CartSkuDto goods = new CartSkuDto();

                        goods.setSpuId(commonSkuEntity.getSpuId());
                        goods.setSkuId(commonSkuEntity.getId());
                        goods.setName(commonSkuEntity.getName());
                        goods.setAttrs(GoodsUtils.getAttrValues(commonSkuEntity.getAttrs()));
                        goods.setImg(commonSkuEntity.getImg());
                        goods.setNum(cartAddVo.getQuantity());
                        goods.setStock(commonSkuEntity.getStock());
                        goods.setPrice(commonSkuEntity.getPrice());
                        goods.setSelected(true);

                        stringRedisTemplate.opsForHash().put(shopKey, skuHashKey, goods);
                    } else {
                        // 更新 shop - sku
                        CartSkuDto goods = (CartSkuDto) goodsObj;
                        Integer num = goods.getNum() + cartAddVo.getQuantity();
                        if (goods.getStock() < num) {
                            throw new CustomException("商品库存不足");
                        }
                        goods.setNum(num);
                        stringRedisTemplate.opsForHash().put(shopKey, skuHashKey, goods);
                    }
                }

                return operations.exec();
            }
        };

        stringRedisTemplate.execute(callback);

        return R.success();
    }

    @Override
    public R<CartInfoResVo> info(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        CartInfoResVo cartInfoResVo = new CartInfoResVo();
        String userKey = RedisKeyConstant.CART_PREFIX + tokenDto.getId();

        List<Boolean> totalSelected = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<CartInfoResVo.ShopItem> shopList = new ArrayList<>();

        Map<Long, CartInfoResVo.ShopItem> userShopMap = calcUserShopMap(userKey);

        for (Map.Entry<Long, CartInfoResVo.ShopItem> userShop : userShopMap.entrySet()) {
            Long shopId = userShop.getKey();
            CartInfoResVo.ShopItem shopItem = userShop.getValue();
            List<Boolean> shopSelected = new ArrayList<>();

            String shopKey = userKey + "_" + shopId;
            List<CartSkuDto> goodsList = calcGoodsList(shopKey);
            for (CartSkuDto goods : goodsList) {
                if (goods.getSelected()) {
                    totalPrice = totalPrice.add(goods.getPrice().multiply(BigDecimal.valueOf(goods.getNum())));
                }
                totalSelected.add(goods.getSelected());
                shopSelected.add(goods.getSelected());
            }

            shopItem.setGoodsList(goodsList);
            shopItem.setSelected(shopSelected.stream().allMatch(selected -> selected));
            shopItem.setIndeterminate(shopSelected.stream().anyMatch(selected -> selected) && shopSelected.stream().anyMatch(selected -> !selected));

            shopList.add(shopItem);
        }

        cartInfoResVo.setTotalPrice(PriceUtils.precision(totalPrice));
        cartInfoResVo.setTotalSelected(totalSelected.stream().allMatch(selected -> selected));
        cartInfoResVo.setIndeterminate(totalSelected.stream().anyMatch(selected -> selected) && totalSelected.stream().anyMatch(selected -> !selected));
        cartInfoResVo.setShopList(shopList);

        return R.success(cartInfoResVo);
    }

    @Override
    public R<Long> getCount(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        Long selectedCount = calcSelectedCount(tokenDto.getId());
        return R.success(selectedCount);
    }

    @Override
    public R<Long> getCountByMemberId(Long memberId) {
        Long selectedCount = calcSelectedCount(memberId);
        return R.success(selectedCount);
    }

    @Override
    public R<Void> del(Long shopId, Long skuId, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        delShopSku(shopId, skuId, tokenDto.getId());
        return R.success("删除成功");
    }

    @Override
    public R<Void> changeSelect(List<ChangeSelectReqVo> changeSelectReqVos, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        for (ChangeSelectReqVo changeSelectReqVo : changeSelectReqVos) {
            Long shopId = changeSelectReqVo.getShopId();
            List<ChangeSelectReqVo.Goods> goodsList = changeSelectReqVo.getGoodsList();

            for (ChangeSelectReqVo.Goods goods : goodsList) {
                String shopKey = RedisKeyConstant.CART_PREFIX + tokenDto.getId() + "_" + shopId;
                String skuHashKey = goods.getSkuId() + "";

                Object goodsObj = stringRedisTemplate.opsForHash().get(shopKey, skuHashKey);
                if (goodsObj != null) {
                    CartSkuDto oldGoods = (CartSkuDto)goodsObj;
                    oldGoods.setSelected(goods.getSelected());
                    stringRedisTemplate.opsForHash().put(shopKey, skuHashKey, oldGoods);
                }
            }
        }

        return R.success();
    }

    @Override
    public R<Void> changeQuantity(ChangeQuantityReqVo changeQuantityReqVo, HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        String shopKey = RedisKeyConstant.CART_PREFIX + tokenDto.getId() + "_" + changeQuantityReqVo.getShopId();
        String skuHashKey = changeQuantityReqVo.getSkuId() + "";

        Object goodsObj = stringRedisTemplate.opsForHash().get(shopKey, skuHashKey);
        if (goodsObj == null) {
            throw new CustomException("不存在该购物车商品");
        }

        CartSkuDto goods = (CartSkuDto) goodsObj;
        if (goods.getStock() < changeQuantityReqVo.getQuantity()) {
            throw new CustomException("商品库存不足");
        }

        goods.setNum(changeQuantityReqVo.getQuantity());
        stringRedisTemplate.opsForHash().put(shopKey, skuHashKey, goods);

        return R.success();
    }

    @Override
    public R<Void> delSelected(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        traversalMemberCart(tokenDto.getId(), (shopKey, skuHashKey, shopId, skuId, cartSkuDto) -> {
            if (cartSkuDto.getSelected()) {
                delShopSku(shopId, skuId, tokenDto.getId());
            }
        });

        return R.success();
    }

    @Override
    public R<List<CartSkuDto>> getCartSkus(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);
        List<CartSkuDto> cartSkuDtos = new ArrayList<>();
        traversalMemberCart(tokenDto.getId(), (shopKey, skuHashKey, shopId, skuId, cartSkuDto) -> cartSkuDtos.add(cartSkuDto));
        return R.success(cartSkuDtos);
    }

    private void traversalMemberCart(Long memberId, ConsumeShopSku css) {
        String userKey = RedisKeyConstant.CART_PREFIX + memberId;
        Set<Object> shopHashKeys = stringRedisTemplate.opsForHash().keys(userKey);

        if (!CollectionUtils.isEmpty(shopHashKeys)) {
            for (Object shopHashKey : shopHashKeys) {
                String shopKey = userKey + "_" + shopHashKey;
                Map<Object, Object> shopSkuEntries = stringRedisTemplate.opsForHash().entries(shopKey);

                if (!shopSkuEntries.isEmpty()) {
                    for (Map.Entry<Object, Object> shopSkuEntry : shopSkuEntries.entrySet()) {
                        CartSkuDto cartSkuDto = (CartSkuDto) shopSkuEntry.getValue();
                        if (cartSkuDto.getSelected()) {
                            String skuHashKey = cartSkuDto.getSkuId() + "";
                            css.consume(shopKey, skuHashKey, Long.valueOf(shopHashKey + ""), Long.valueOf(skuHashKey), cartSkuDto);
                        }
                    }
                }
            }
        }
    }

    private Map<Long, CartInfoResVo.ShopItem> calcUserShopMap (String userKey) {
        Map<Long, CartInfoResVo.ShopItem> userShopMap = new HashMap<>();

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(userKey);
        if (!entries.isEmpty()) {
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                Long shopId = Long.parseLong(entry.getKey() + "");
                CartInfoResVo.ShopItem shopItem = (CartInfoResVo.ShopItem) entry.getValue();
                userShopMap.put(shopId, shopItem);
            }
        }

        return userShopMap;
    }

    private List<CartSkuDto> calcGoodsList(String shopKey) {
        List<Object> shopSkus = stringRedisTemplate.opsForHash().values(shopKey);
        if (!CollectionUtils.isEmpty(shopSkus)) {
            return shopSkus.stream().map(shopSku -> (CartSkuDto)shopSku).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Long calcSelectedCount(Long memberId) {
        String userKey = RedisKeyConstant.CART_PREFIX + memberId;
        Map<Long, CartInfoResVo.ShopItem> userShopMap = calcUserShopMap(userKey);
        Long total = 0L;

        for (Map.Entry<Long, CartInfoResVo.ShopItem> userShop : userShopMap.entrySet()) {
            total += stringRedisTemplate.opsForHash().size(userKey + "_" + userShop.getKey());
        }

        return total;
    }

    private void delShopSku(Long shopId, Long skuId, Long memberId) {

        String userKey = RedisKeyConstant.CART_PREFIX + memberId;
        String shopHashKey = shopId + "";

        String shopKey = userKey + "_" + shopId;
        String skuHashKey = skuId + "";

        if (!stringRedisTemplate.opsForHash().hasKey(shopKey, skuHashKey)) {
            throw new CustomException("不存在该购物车商品");
        }

        if (stringRedisTemplate.opsForHash().delete(shopKey, skuHashKey) != 1) {
            throw new CustomException("删除商品失败");
        }

        if (stringRedisTemplate.opsForHash().size(shopKey) == 0) {
            if (stringRedisTemplate.opsForHash().hasKey(userKey, shopHashKey) && stringRedisTemplate.opsForHash().delete(userKey, shopHashKey) != 1) {
                throw new CustomException("删除商品所属店铺失败");
            }
        }
    }

    @FunctionalInterface
    interface ConsumeShopSku {
        void consume(String shopKey, String skuHashKey, Long shopId, Long skuId, CartSkuDto cartSkuDto);
    }
}