# Memo

## 技术栈：

Spring+SpringBoot+SpringSecurity+mqsql+mybatisplus+redis+nacos+gateway

## 目录结构介绍

```SQL
└─huayu
    └─api
        ├─domain                --实体类包
        ├─dto                   --dto传输数据包
        ├─exception             --自定义异常包
        ├─mapper                --数据层包
        ├─utils                 --工具包
    └─auth
        ├─config                --配置包
        ├─controller            --表现层包
        ├─handler               --处理器包
        ├─service               --业务层包
               └─impl           --业务层实现包 
        ├─utils                 --工具包 
    └─gateway
        ├─config                --配置包
        ├─filters               --过滤器包
    └─ratelimit
        ├─annotation            --注解实体类包
        ├─aspect                --切面包
        ├─config                --配置包    
        ├─controller            --表现层包
        ├─core                  --核心配置加载包
        ├─exception             --自定义异常包
        ├─spi                   --限流算法包
    └─task
        ├─controller            --表现层包
        ├─domain                --实体类包
        ├─mapper                --数据层包
        ├─service               --业务层包
               └─impl           --业务层实现包 
    └─user
        ├─config                --配置包
        ├─controller            --表现层包
        ├─service               --业务层包
               └─impl           --业务层实现包 
 
```

## 功能介绍

该项目可以让用户创建属于自己的代办，设定代办的完成，以及删除不需要的代办等内容

## 项目亮点

使用微服务架构，网关登录校验，自定义限流注解等技术

## 待改进点

整合链路追踪，服务熔断，缓存等内容

## 如何启动

获得项目的jar包后为每个jar包编写dockerfile文件，编写docker-compose文件部署后可启动，需要nacos，mysql，redis环境