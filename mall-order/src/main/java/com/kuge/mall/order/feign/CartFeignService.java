package com.kuge.mall.order.feign;

import com.kuge.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * created by xbxie on 2024/7/1
 */
@FeignClient("mall-cart")
public interface CartFeignService {
    @PostMapping(value = "/cart/delSelected")
    R<Void> delSelected();
}