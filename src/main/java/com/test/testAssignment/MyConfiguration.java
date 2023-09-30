package com.test.testAssignment;

import com.test.testAssignment.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.test.testAssignment")
public class MyConfiguration {
    @Bean
    public UserService UserService() {
        return new UserService();
    }
}