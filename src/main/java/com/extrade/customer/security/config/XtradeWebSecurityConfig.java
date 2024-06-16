package com.extrade.customer.security.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class XtradeWebSecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
          return http.authorizeHttpRequests((requests) -> {
             requests
                     .requestMatchers("/home",
                             "/static/**",
                             "/customer/signup",
                             "/customer/**")
                     .permitAll().anyRequest().authenticated();

          }).csrf(csrf -> {
              csrf.disable();
          }).formLogin((formLoginConfigurer)->{
              formLoginConfigurer.loginPage("/customer/login")  // if u dont specify custom url(if u remove->.loginPage("/customer-login")) then spring generates by default login page
                      .permitAll()
                      .usernameParameter("j_username")
                      .passwordParameter("j_password")
                      .loginProcessingUrl("/customer/j_login") //when u submit the login form it will goes to j_login,This URL is typically handled by Spring Security's authentication mechanisms
                      .defaultSuccessUrl("/home") //upon login is success ,redirect to home
              .failureHandler(authenticationFailureHandler());
          }).logout(logout->{
              logout
                      .logoutSuccessUrl("/customer/logout")  //if springsecurity logout successfully then re-direct to /customer/logout
                      .invalidateHttpSession(true)
                      .permitAll();

          }).build();
    }

//QUESTION:
//means that if any one trying to access authenticated resource other than
// "/home", "/static/**", "/customer/signup") then formLogin will execute and redirect
// the request to custom login url "customer/login"
//ANSWER:
//->Authorization Configuration:
    //Requests to /home, URLs under /static/, and /customer/signup are permitted without authentication (permitAll()).
    //Any other request not matching these patterns requires authentication (authenticated()).
//->FormLogin Configuration:
    //If a user tries to access a resource that requires authentication
    // (i.e., any resource not covered by the permitAll()), and they are not logged in,
    // Spring Security will redirect them to the custom login page specified at /customer/login.
    //The .permitAll() configuration for the login page ensures that users can access the
    // login page without being authenticated. This is necessary so that users who are not yet
    // authenticated can access the login form.
//--------------------------------------------------------------------------------------------

//whenever there is failure,spring security handler is going to throw different different type of exception
//for each type of exception we are mapping it to handler
@Bean
 public AuthenticationFailureHandler authenticationFailureHandler(){
    ExceptionMappingAuthenticationFailureHandler failureHandler=null;
    Properties exceptionMappings=null;

    failureHandler=new ExceptionMappingAuthenticationFailureHandler();  //spring-security-handler object in which we will map exception
    exceptionMappings=new Properties();

    exceptionMappings.put("org.springframework.security.authentication.BadCredentialsException","/customer/login?error=bad"); //if authentication-handler is throwig this exception,then forward the user to this url by attaching query parameter (error=bad)
    //when we submit login page and any exception occur while submitting,then it will redirect to "/customer/login with query param error=bad".
    //again we will land to login page but this time we have query param(error=bad) with redirected url and we need to display it in the login page .-->see login page code
    exceptionMappings.put("org.springframework.security.authentication.DisabledException","/customer/login?error=disabled");
    exceptionMappings.put("org.springframework.security.authentication.LockedException","/customer/login?error=locked");
    failureHandler.setExceptionMappings(exceptionMappings);
     return failureHandler;
  }
}