package com.huayu.ratelimit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 限流注解redis配置类
 */
public class RateLimitAutoConfiguration {
    /**
     * 模块自包含redis连接
     *
     * @param factory bean工厂
     * @return redis客户端对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        // 设置其他序列化器...
        return template;
    }
    // 其他配置...
}
