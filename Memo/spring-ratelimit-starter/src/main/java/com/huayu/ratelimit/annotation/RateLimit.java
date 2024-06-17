package com.huayu.ratelimit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解属性
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    RateLimitPolicy[] policy() default {};
}

