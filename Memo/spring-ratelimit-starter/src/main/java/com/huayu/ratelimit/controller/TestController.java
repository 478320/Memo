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


    @GetMapping()
    public String login() {
        //登录
        return "hi";
    }
}
