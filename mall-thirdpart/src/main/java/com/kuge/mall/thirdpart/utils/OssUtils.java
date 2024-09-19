package com.kuge.mall.thirdpart.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.kuge.mall.thirdpart.config.OssConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * created by xbxie on 2024/5/17
 */
@Component
public class OssUtils {
    @Resource
    private OssConfig ossConfig;

    public String uploadImg(MultipartFile multipartFile) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

        try {
            String originalFilename = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            String objectName = "mall/" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectName, inputStream);
            ossClient.putObject(putObjectRequest);
            return ossConfig.getBucketDomain() + "/" + objectName;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return "";
    }
}
