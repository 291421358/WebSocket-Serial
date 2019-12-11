package com.laola.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring boot 启动类。
 * spring boot 项目搭建简单方法
 * 新建空项目
 * 创建/src/main/java/com/sb/sbb目录
 * 创建resources目录，
 * 创建application.yml 配置文件
 * 新建pom文件
 * 引入 org.springframwork.boot spring-boot-start-web，spring-boot-start-parent依赖，方可启动
 */
@SpringBootApplication
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class,args);
    }
}
