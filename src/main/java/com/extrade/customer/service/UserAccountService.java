package com.extrade.customer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="userAccountService", url ="${userAccountService.url}/account")
//1.Spring will replace ${userAccountService.url} with the value specified in your application.properties file (http://localhost:8090/
public interface UserAccountService {
    //writting url to connect microservice in application.properties
//    userAccountService:
//        url: http://localhost:8090
    @GetMapping("/count/email")
    Long countUserAccountByEmailService(@RequestParam("emailAddress") String emailAddress);
    @GetMapping("/count/mobileNo")
    Long countUserAccountByMobileNo(@RequestParam("mobileNo") String mobileNo);

    //now we will inject this service into CustomerSignupFormValidator and into controller class


}
