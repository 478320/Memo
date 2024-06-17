package com.huayu.ratelimit.spi.impl;

import com.huayu.ratelimit.exception.RateLimitException;
import com.huayu.ratelimit.spi.RateLimitAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 *
 */
public class CustomRateLimitAlgorithm implements RateLimitAlgorithm {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Long> LIMIT_SCRIPT;

    // 加载lua脚本
    static {
        LIMIT_SCRIPT = new DefaultRedisScript<>();
        LIMIT_SCRIPT.setLocation(new ClassPathResource("rate_limit.lua"));
        LIMIT_SCRIPT.setResultType(Long.class);
    }
    @Override
    public boolean allowRequest(String key, int maxCount, long timeRange, TimeUnit timeUnit) {
        // 检查访问次数
        int size = stringRedisTemplate.keys(key+"*").size();
        long seconds = timeUnit.toSeconds(timeRange);
        if (size < maxCount) {
            stringRedisTemplate.execute(LIMIT_SCRIPT,
                    Collections.singletonList(key+ UUID.randomUUID()),
                    String.valueOf(seconds));

        } else{
            throw new RateLimitException(403,"请求过于频繁请稍后再试");
        }
        return true;
    }

}
