package com.awe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.awe.mapper")
@SpringBootApplication
public class AweApplication {
    public static void main(String[] args) {
        SpringApplication.run(AweApplication.class, args);
    }
}
