package com.extrade.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableFeignClients
@SpringBootApplication
public class CustomerApplication implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/shop").setViewName("full-page-layout");
        registry.addViewController("/customer/signup").setViewName("register-customer");
        registry.addViewController("/customer/login").setViewName("customer-login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/customer/logout").setViewName("logout");
     }
}
