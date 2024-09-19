package com.kuge.mall.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.AuthConstant;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.R;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024/5/22
 */
@Data
@Component
@ConfigurationProperties(prefix = "interceptor.path")
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private List<String> excludeList;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 不需要 token 的路径
        String requestPath = request.getPath().toString();
        if (excludeList.stream().anyMatch(path -> antPathMatcher.match(path, requestPath))) {
            System.out.println("不需要 token");
            return chain.filter(exchange);
        }


        System.out.println("需要 token");
        String authorization = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        String token = StringUtils.hasLength(authorization) ? authorization.replace(AuthConstant.JWT_TOKEN_PREFIX, "") : "";

        if (!StringUtils.hasLength(token)) {
            System.out.println("未携带 token");
            DataBuffer dataBuffer = genBuffer(response, R.fail(401, "token验证失败"));
            return response.writeWith(Mono.just(dataBuffer));
        }


        System.out.println("token = " + token);
        TokenDto tokenDto;
        try {
            tokenDto = JwtUtils.parseToken(token);
        } catch (Exception exception) {
            DataBuffer dataBuffer = genBuffer(response, R.fail(401, "token验证失败"));
            return response.writeWith(Mono.just(dataBuffer));
        }
        
        String redisTokenKey;
        if (requestPath.startsWith("/admin")) {
            redisTokenKey = AuthConstant.REDIS_TOKEN_ADMIN_PREFIX + tokenDto.getId();
        } else {
            redisTokenKey = AuthConstant.REDIS_TOKEN_MEMBER_PREFIX + tokenDto.getId();
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(redisTokenKey))) {
            System.out.println("携带了需要的token，但在redis不存在" + redisTokenKey);
            DataBuffer dataBuffer = genBuffer(response, R.fail(401, "token已失效，请重新登录"));
            return response.writeWith(Mono.just(dataBuffer));
        }


        if (
            requestPath.startsWith("/admin")
            && ((requestPath.contains("/add") || requestPath.contains("/update") || requestPath.contains("/del"))|| requestPath.contains("/changeStatus"))
            && !Objects.equals(tokenDto.getAccount(), "18755781039")
        ) {
            DataBuffer dataBuffer = genBuffer(response, R.fail(403, "演示账号暂无权限~"));
            return response.writeWith(Mono.just(dataBuffer));
        }

        System.out.println("权限正常");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private DataBuffer genBuffer(ServerHttpResponse response, R<Void> r) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        byte[] bytes = JSONObject.toJSONString(r).getBytes(StandardCharsets.UTF_8);
        return response.bufferFactory().wrap(bytes);
    }
}
