package com.huayu.ratelimit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimitPolicy {
    // 标识前缀key
    String prefixKey() default "";

    // 频控时间范围
    int timeRange() default 0;

    // 频控时间单位
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    // 单位频控时间范围内最大访问次数
    int maxCount() default 0;
}
