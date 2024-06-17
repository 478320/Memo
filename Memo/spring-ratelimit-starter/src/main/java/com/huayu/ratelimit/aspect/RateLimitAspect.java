package com.huayu.ratelimit.aspect;

import com.huayu.ratelimit.annotation.RateLimit;
import com.huayu.ratelimit.annotation.RateLimitPolicy;
import com.huayu.ratelimit.core.RateLimitConfigProperties;
import com.huayu.ratelimit.exception.RateLimitException;
import com.huayu.ratelimit.spi.RateLimitAlgorithm;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解切面类
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitConfigProperties rateLimitProperties;

    private final RateLimitAlgorithm rateLimitAlgorithm;
    // 设定切面
    @Pointcut("@annotation(com.huayu.ratelimit.annotation.RateLimit)")
    public void rateLimitPointcut() {}

    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解内部的属性
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        for (RateLimitPolicy policy : rateLimit.policy()) {
            int timeRange = policy.timeRange() > 0 ? policy.timeRange() : rateLimitProperties.getTimeRange();
            TimeUnit timeUnit = policy.timeUnit() != TimeUnit.MILLISECONDS ? policy.timeUnit() : rateLimitProperties.getTimeUnit();
            int maxCount = policy.maxCount() > 0 ? policy.maxCount() : rateLimitProperties.getMaxCount();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // 获取到使用用户的IP地址
            String requestIp = request.getRemoteAddr();
            //判断地址是否有效
            if (StringUtil.isNullOrEmpty(requestIp)){
                throw new RateLimitException(403,"无效的IP地址");
            }
            String uri = request.getRequestURI();
            String key = policy.prefixKey() + uri + "_" + requestIp + "_";
            rateLimitAlgorithm.allowRequest(key,maxCount,timeRange,timeUnit);
        }
        return joinPoint.proceed();
    }
}
