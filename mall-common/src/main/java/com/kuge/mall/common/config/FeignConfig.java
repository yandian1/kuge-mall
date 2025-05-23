package com.kuge.mall.common.config;

import com.kuge.mall.common.constant.AuthConstant;
import com.kuge.mall.common.utils.JwtUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * created by xbxie on 2024/8/4
 */
@Configuration
public class FeignConfig {

    @Bean("requestInterceptor")
    public static RequestInterceptor requestInterceptor() {
        // 创建拦截器
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 1、使用RequestContextHolder拿到原生请求的请求头信息（下文环境保持器）
                // 从ThreadLocal中获取请求头（要保证feign调用与controller请求处在同一线程环境）
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    // 获取controller请求对象
                    HttpServletRequest request = requestAttributes.getRequest();
                    // 如果使用线程池进行远程调用，则request是空的（因为RequestContextHolder.getRequestAttributes()是从threadlocal里拿的值）
                    if (Objects.nonNull(request)) {
                        String token = JwtUtils.getToken(request);
                        if (StringUtils.hasLength(token)) {
                            template.header(AuthConstant.JWT_TOKEN_HEADER, token);
                        }
                        //2、获取老请求里的cookie信息
                        // String cookie = request.getHeader("Cookie");
                        // // 同步Cookie （将老请求里的cookie信息放入新请求里（RequestTemplate））
                        // template.header("Cookie", cookie);
                    }
                }
            }
        };
    }
}

