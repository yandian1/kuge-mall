package com.kuge.mall.order.feign;

import com.kuge.mall.common.dto.CartSkuDto;
import com.kuge.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xbxie on 2024/7/1
 */
@FeignClient("mall-cart")
public interface CartRedisFeignService {
    @PostMapping(value = "/cart/redis/delSelected")
    R<Void> delSelected();

    @PostMapping(value = "/cart/redis/cartSkus")
    R<List<CartSkuDto>> getCartSkus();
}