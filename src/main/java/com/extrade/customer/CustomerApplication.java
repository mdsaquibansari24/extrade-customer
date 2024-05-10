package com.extrade.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CustomerApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);

    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/shop").setViewName("shop");
//        registry.addViewController("/register").setViewName("register-customer");
//     }
}
