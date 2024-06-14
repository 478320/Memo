package com.huayu.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 路径加载类
 */
@Data
@Component
@ConfigurationProperties(prefix = "memo.auth")
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
