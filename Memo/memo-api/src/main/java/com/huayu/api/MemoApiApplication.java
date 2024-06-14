package com.huayu.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class MemoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoApiApplication.class, args);
    }

}
