package com.huayu.gateway.filters;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.huayu.api.exception.BusinessException;
import com.huayu.api.utils.JwtUtil;
import com.huayu.gateway.config.AuthProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.huayu.api.utils.Code.BUSINESS_ERR;
import static com.huayu.api.utils.RedisConstans.LOGIN_USER_KEY;

/**
 * 实现网关登录授权全局过滤器
 */
@Component
@RequiredArgsConstructor
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取Request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否不需要拦截
        if(isExclude(request.getPath().toString())){
            // 无需拦截，直接放行
            return chain.filter(exchange);
        }
        // 3.获取请求头中的token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (!CollUtil.isEmpty(headers)) {
            token = headers.get(0);
        }
        // 4.校验并解析token
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BUSINESS_ERR, "token非法");
        }
        //从redis中获取用户信息
        String redisKey = LOGIN_USER_KEY + userId;
        String loginUserStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isBlank(loginUserStr)) {
            throw new RuntimeException("用户未登录");
        }
        ServerWebExchange swe = exchange.mutate().
                request(builder -> builder.header("user-info", loginUserStr))
                .build();
        // 6.放行
        return chain.filter(swe);
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, antPath)){
                return true;
            }
        }
        return false;
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
