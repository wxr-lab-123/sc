package com.hjm.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.hjm.cart.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.hjm.api.client")
public class CartlApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartlApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new  RestTemplate();
    }
}