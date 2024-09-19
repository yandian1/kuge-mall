package com.kuge.mall.common.utils;

import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.constant.AuthConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * created by xbxie on 2024/5/13
 */
public class JwtUtils {
    private static final String SIGNING_KEY = "yG1RwgQhzAeQ57Z4g0hM";

    public static String createToken(TokenDto tokenDto) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", tokenDto.getId());
        map.put("name", tokenDto.getName());
        map.put("account", tokenDto.getAccount());

        return Jwts.builder().setClaims(map).signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    public static TokenDto parseToken(String token) {
        TokenDto tokenDto = new TokenDto();
        Map<String, Object> map = Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();

        Object idObj = map.get("id");
        if (idObj != null) {
            Long id = idObj instanceof Integer ? Long.parseLong(idObj.toString()) : (Long)idObj;
            tokenDto.setId(id);
        }
        String name = (String) map.get("name");
        if (StringUtils.hasLength(name)) {
            tokenDto.setName(name);
        }
        String account = (String) map.get("account");
        if (StringUtils.hasLength(account)) {
            tokenDto.setAccount(account);
        }

        return tokenDto;
    }

    public static TokenDto parseToken(HttpServletRequest request) {
        String token = getToken(request);
        if(!StringUtils.hasLength(token)) {
            return null;
        }

        return parseToken(token);
    }

    public static String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        if(!StringUtils.hasLength(authorization)) {
            return null;
        }

        String token = authorization.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        if(!StringUtils.hasLength(token)) {
            return null;
        }

        return token;
    }
}
