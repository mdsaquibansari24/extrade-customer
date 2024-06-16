package com.extrade.customer.service;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.UserAccountDto;
import com.extrade.customer.dto.UserRegistrationDto;
import com.extrade.customer.feign.config.UserAccountServiceFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="userAccountService", url ="${userAccountService.url}/account",configuration = UserAccountServiceFeignConfiguration.class)
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

    @PostMapping(value="/customer", consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    Long registerUser(UserRegistrationDto userRegistrationDto);


    //Mobile OTP verification and Email otp verification yahin se hoga
    // I AM PUTTING THE OTP,VERIFICATION_TYPE,AND USER ACCOUNTID THAT WILL GIVE STATUS DTO
    @PutMapping(value = "{userAccountId}/{otpCode}/{verificationType}",produces = {MediaType.APPLICATION_JSON_VALUE})
    //in the case of "verifyOTP" we will not always get "AccountVerificationStatusDto" only
    //we can get diff-diff error-responses also and responses also
    //one way of resolving this ,instead of "AccountVerificationStatusDto" we write ResponseEntity<?>,but there is problem with this  is even exception will be produced as wriiten value, and we need to extract the exception in controller class with conditions ,which is complex
    //solution:instead of handling situation with ResponseEntity,we are telling "Feign-decoder" that ,in case of success give a userAccountStatus but in case of failure,,we dont want return value
    //this proxy(Feign-clint-interface)should use ErrorDecoder that we configured in feign.config package,how do we tell that?Ans: see lineNo-9
    AccountVerificationStatusDto verifyOTP(@PathVariable("userAccountId") Long userAccountId,
                                           @PathVariable("otpCode") String otpCode,
                                           @PathVariable("verificationType") String verificationType);


    @GetMapping(value = "/{userAccountId}/verificationStatus", consumes = {MediaType.APPLICATION_JSON_VALUE})
    AccountVerificationStatusDto accountVerificationStatusDto(@PathVariable("userAccountId") Long userAccountId);


    //security impl: when u input emailAddress, it will bring out password and verify it
    @GetMapping(value = "/details", consumes = {MediaType.APPLICATION_JSON_VALUE})
    UserAccountDto getUserByEmailAddress(@RequestParam("emailAddress") String emailAddress);
}
