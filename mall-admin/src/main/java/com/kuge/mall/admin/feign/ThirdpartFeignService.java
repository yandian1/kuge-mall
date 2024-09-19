package com.kuge.mall.admin.feign;

import com.kuge.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * created by xbxie on 2024/7/1
 */
@FeignClient("mall-thirdpart")
public interface ThirdpartFeignService {
    @PostMapping(value = "/upload/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<Map<String, String>> updateImg(@RequestPart(value = "file") MultipartFile file);
}