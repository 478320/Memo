package com.huayu.ratelimit.config;

import com.huayu.ratelimit.core.RateLimitConfigProperties;
import com.huayu.ratelimit.spi.RateLimitAlgorithm;
import com.huayu.ratelimit.spi.impl.CustomRateLimitAlgorithm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 限流注解redis配置类
 */
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
@EnableConfigurationProperties(RateLimitConfigProperties.class)
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

    @Bean
    @ConditionalOnProperty(name = "rate.limit.strategy", havingValue = "custom", matchIfMissing = true)
    public RateLimitAlgorithm customRateLimitAlgorithm() {
        return new CustomRateLimitAlgorithm();
    }
    // 其他配置...
}
