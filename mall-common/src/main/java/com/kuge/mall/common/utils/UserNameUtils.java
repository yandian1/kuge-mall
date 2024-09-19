package com.kuge.mall.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuge.mall.common.entity.CommonMemberEntity;
import com.kuge.mall.common.service.CommonMemberService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * created by xbxie on 2024/6/6
 */
public class UserNameUtils {
    private static CommonMemberService commonMemberService;

    public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//可选字

    static {
        if (commonMemberService == null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
            commonMemberService = context.getBean(CommonMemberService.class);
        }
    }


    // 随机生成字符串
    private static String genName(){

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 20; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }


    public static String genUniqueName(){
        String name = genName();

        while (
            commonMemberService.exists(
                new QueryWrapper<CommonMemberEntity>()
                    .eq("name", genName())
            )
        ){
            name = genName();
        }

        return name;
    }
}
