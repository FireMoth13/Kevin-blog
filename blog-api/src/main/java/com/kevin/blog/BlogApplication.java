package com.kevin.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kevin.blog")
@MapperScan("com.kevin.blog.modules.**.mapper")
public class BlogApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(BlogApplication.class, args);
    }
}
