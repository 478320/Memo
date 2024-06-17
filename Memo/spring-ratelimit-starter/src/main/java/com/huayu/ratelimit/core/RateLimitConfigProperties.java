package com.huayu.ratelimit.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@ConfigurationProperties(prefix = "rate.limit")
@Data
public class RateLimitConfigProperties {
    private int timeRange = 60;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private int maxCount = 100;
}
