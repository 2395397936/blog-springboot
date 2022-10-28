package com.example.user;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.user.mapper")
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.example"})
public class UserApp {

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }

}
