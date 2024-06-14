package com.huayu.ratelimit.controller;

import com.huayu.ratelimit.annotation.RateLimit;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 测试表现层
 */
@RestController
@RequestMapping("/hi")
public class TestController {

    @RateLimit(prefixKey = "list",timeRange = 10,timeUnit = TimeUnit.SECONDS,maxCount = 3)
    @GetMapping()
    public String login() {
        //登录
        return "hi";
    }
}
