package com.example.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@MapperScan("com.example.blog.mapper")
@EnableDiscoveryClient
public class BlogApp {

    public static void main(String[] args) {
        SpringApplication.run(BlogApp.class, args);
    }

}
