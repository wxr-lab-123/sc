package com.hjm.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hjm.item.mapper")
@SpringBootApplication
public class itemApplication {
    public static void main(String[] args) {
        SpringApplication.run(itemApplication.class, args);
    }
}