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

    // 标识前缀key
    String prefixKey() default "";

    // 频控时间范围
    int timeRange();

    // 频控时间单位
    TimeUnit timeUnit();

    // 单位频控时间范围内最大访问次数
    int maxCount();
}
