package com.kuge.mall.admin.controller;

import com.kuge.mall.admin.feign.ThirdpartFeignService;
import com.kuge.mall.common.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 文件上传控制器
 * created by xbxie on 2024/5/13
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Resource
    private ThirdpartFeignService thirdpartFeignService;

    @PostMapping("/img")
    public R<Map<String, String>> uploadImg(@RequestParam("file") MultipartFile multipartFile) {
        R<Map<String, String>> mapR = thirdpartFeignService.updateImg(multipartFile);
        return R.success(mapR.getData());
    }
}
