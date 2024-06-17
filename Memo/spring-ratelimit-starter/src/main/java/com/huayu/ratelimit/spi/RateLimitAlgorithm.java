package com.huayu.ratelimit.spi;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public interface RateLimitAlgorithm {

    boolean allowRequest(String key, int maxCount, long timeRange, TimeUnit timeUnit);

}
