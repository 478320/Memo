package com.huayu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.huayu.api.mapper")
@SpringBootApplication
public class MemoAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoAuthApplication.class, args);
    }

}
