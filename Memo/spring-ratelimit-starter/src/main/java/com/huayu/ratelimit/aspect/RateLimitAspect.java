package com.huayu.ratelimit.aspect;

import com.huayu.ratelimit.annotation.RateLimit;
import com.huayu.ratelimit.exception.RateLimitException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

/**
 * 限流注解切面类
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private static final DefaultRedisScript<Long> LIMIT_SCRIPT;

    // 加载lua脚本
    static {
        LIMIT_SCRIPT = new DefaultRedisScript<>();
        LIMIT_SCRIPT.setLocation(new ClassPathResource("rate_limit.lua"));
        LIMIT_SCRIPT.setResultType(Long.class);
    }

    private final StringRedisTemplate stringRedisTemplate;

    // 设定切面
    @Pointcut("@annotation(com.huayu.ratelimit.annotation.RateLimit)")
    public void rateLimitPointcut() {}

    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解内部的属性
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        long maxCount = rateLimit.maxCount();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 获取到使用用户的IP地址
        String requestIp = request.getRemoteAddr();
        //判断地址是否有效
        if (StringUtil.isNullOrEmpty(requestIp)){
            throw new RateLimitException(403,"无效的IP地址");
        }
        String uri = request.getRequestURI();
        String key = rateLimit.prefixKey() + uri + "_" + requestIp + "_";
        // 检查访问次数
        int size = stringRedisTemplate.keys(key+"*").size();
        long seconds = rateLimit.timeUnit().toSeconds(rateLimit.timeRange());
        if (size < maxCount) {
            stringRedisTemplate.execute(LIMIT_SCRIPT,
                    Collections.singletonList(key+UUID.randomUUID()),
                    String.valueOf(seconds));

        } else{
            throw new RateLimitException(403,"请求过于频繁请稍后再试");
        }
        return joinPoint.proceed();
    }
}
