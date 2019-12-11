package com.laola.hello.laola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.laola.hello.laola.mapper")
public class LaolaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LaolaApplication.class,args);
    }
}
