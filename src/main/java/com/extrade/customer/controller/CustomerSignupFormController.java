package com.extrade.customer.controller;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.UserRegistrationDto;
import com.extrade.customer.exception.OTPAlreadyVerifiedException;
import com.extrade.customer.exception.OTPMisMatchException;
import com.extrade.customer.exception.UserAccountAlreadyActivatedException;
import com.extrade.customer.exception.UserAccountNotFoundException;
import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.form.MobileOTPVerificationForm;
import com.extrade.customer.service.UserAccountService;
import com.extrade.customer.util.StringUtil;
import com.extrade.customer.util.XtradeConstrants;
import com.extrade.customer.validator.CustomerSignupFormValidator;
import com.extrade.customer.validator.MobileOTPVerificationFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/customer")
public class CustomerSignupFormController {
    private final CustomerSignupFormValidator customerSignupFormValidator;
    private final MobileOTPVerificationFormValidator mobileOTPVerificationFormValidator;
    private final UserAccountService userAccountService;
    private final MessageSource messageSource;

    //This code is saying,
    // "Hey Spring, whenever you're converting form data into Java objects(customerSignupForm object),
    // I want you to use this customerSignupFormValidator to make sure the data is valid."


    //lec-39 20:42 is the reason to comment this out,iske badle me line 43 44 aya
         //    @InitBinder
         //    public void initBinder(WebDataBinder binder){
        //        binder.addValidators(customerSignupFormValidator);
        //    }

    @GetMapping("/signup")
    public String showCustomerSignupForm(Model model){
        model.addAttribute("customerSignupForm",new CustomerSignupForm());
        return "register-customer";
    }
    @PostMapping("/signup")
    public String doSignup(@ModelAttribute("customerSignupForm")  CustomerSignupForm customerSignupForm, BindingResult errors, Model model){
        if(customerSignupFormValidator.supports(customerSignupForm.getClass())){
            customerSignupFormValidator.validate(customerSignupForm,errors);
        }

        UserRegistrationDto userRegistrationDto=null;
        if(errors.hasErrors()){
            return "register-customer";
        }
        userRegistrationDto=new UserRegistrationDto();
        userRegistrationDto.setFirstName(customerSignupForm.getFirstName());
        userRegistrationDto.setLastName(customerSignupForm.getLastName());
        userRegistrationDto.setEmailAddress(customerSignupForm.getEmailAddress());
        userRegistrationDto.setMobileNo(customerSignupForm.getMobileNo());
        userRegistrationDto.setDob(customerSignupForm.getDob());
        userRegistrationDto.setGender(customerSignupForm.getGender());
        userRegistrationDto.setPassword(customerSignupForm.getPassword());

        //Calling service class method to make microservice api call by sending dto
        Long customerId=userAccountService.registerUser(userRegistrationDto);

        //creating object of MobileOTPVerificationForm
        MobileOTPVerificationForm verificationForm=new MobileOTPVerificationForm();
        //customer-signup-otp-verification page display hone se pahle ,signupForm me jo data binded hai
        //usko MobileOTPVerificationForm object me set karna hai so that some information about the user can be
        //displayed on the "customer-signup-otp-verification.html"
        verificationForm.setUserAccountId(customerId);   //register karne se ek id generate hua (line-53)
        verificationForm.setCustomerName(customerSignupForm.getFirstName());
        verificationForm.setVerificationMobileNo(StringUtil.maskMobileNo(4,customerSignupForm.getMobileNo()));
        verificationForm.setVerificationEmailAddress(StringUtil.maskEmailAddress(customerSignupForm.getEmailAddress()));

        model.addAttribute("MobileOTPVerificationForm",verificationForm);

//        //this is working but i want mask part of emailAddress and phone number
//        model.addAttribute("userName",customerSignupForm.getFirstName());
//        model.addAttribute("emailAddress",StringUtil.maskEmailAddress(customerSignupForm.getEmailAddress()));
//        model.addAttribute("mobileNo", StringUtil.maskMobileNo(4,customerSignupForm.getMobileNo()));
//        model.addAttribute("customerId",customerId);
        return "customer-signup-otp-verification";
    }


    @PostMapping("/signup/verifyMobileOTP")
    public String verifyMobileOTP(@ModelAttribute("MobileOTPVerificationForm") MobileOTPVerificationForm mobileOTPVerificationForm
            , BindingResult errors, Model model, Locale locale){
        AccountVerificationStatusDto accountVerificationStatusDto=null;
        String signupStatusMessage=null;

        if(mobileOTPVerificationFormValidator.supports((mobileOTPVerificationForm.getClass()))){
            mobileOTPVerificationFormValidator.validate(mobileOTPVerificationForm,errors);
            if(errors.hasErrors()){
                return "customer-signup-otp-verification";
            }
        }

        //agar success hua to dto me status ayega ,if error-response aya to ErrorDecoder exception dega
       try {
           accountVerificationStatusDto = userAccountService.verifyOTP(mobileOTPVerificationForm.getUserAccountId(),mobileOTPVerificationForm.getOtpCode(),"VERIFICATION_TYPE_MOBILE");
           if(accountVerificationStatusDto.getAccountStatus().equals(XtradeConstrants.USER_ACCOUNT_ACTIVE)){
               //we are using MessageSource to get message from message.properties
               signupStatusMessage=messageSource.getMessage("userAccount.activated",null,locale);
           } else if (accountVerificationStatusDto.getEmailVerificationStatus()==0) {
              signupStatusMessage= messageSource.getMessage("userAccount.mobileOTPVerified",null,locale);

           }

       }catch(OTPMisMatchException e){
           errors.reject("otpCode.mismatch");
       }catch (OTPAlreadyVerifiedException e) {
           accountVerificationStatusDto = userAccountService.accountVerificationStatusDto(mobileOTPVerificationForm.getUserAccountId());
           if (accountVerificationStatusDto.getAccountStatus().equals(XtradeConstrants.USER_ACCOUNT_ACTIVE)) {
               //we are using MessageSource to get message from message.properties
               signupStatusMessage = messageSource.getMessage("userAccount.alreadyActivated", null, locale);
           } else if (accountVerificationStatusDto.getEmailVerificationStatus() == 0) {
               signupStatusMessage = messageSource.getMessage("userAccount.mobileOTPVerified", null, locale);

           }
       }catch (UserAccountAlreadyActivatedException e){
           signupStatusMessage=messageSource.getMessage("userAccount.alreadyActivated",null,locale);
       }
       if(errors.hasErrors()){
           return "customer-signup-otp-verification";
       }
       model.addAttribute("signupStatus",signupStatusMessage);
        return "signup-status";
    }

    //when we click on email link,request comes here
    //xtradeCustomerWebLink + "/customer/" + userAccountId + "/" + emailVerificationOtpCode + "/verifyEmail"
    // http:localhost:8088/customer/24/1QDQ8e/verifyEmail
    @GetMapping("/{userAccountId}/{verificationCode}/verifyEmail")
    public String verifyEmailAddress(@PathVariable("userAccountId") Long userAccountId,
                                     @PathVariable("verificationCode") String verificationCode,Locale locale,Model model){
        AccountVerificationStatusDto accountVerificationStatusDto=null;
        MobileOTPVerificationForm mobileOTPVerificationForm=null;
        boolean mobileOTPVerificationPending=false;
        String signupStatusMessage=null;
        String outcome="signup-status";
        try {
            accountVerificationStatusDto = userAccountService.verifyOTP(userAccountId, verificationCode, "VERIFICATION_TYPE_EMAIL");
            //log.info("upon clicking the email link userAccountStatus: {}",accountVerificationStatusDto.getAccountStatus());
            if (accountVerificationStatusDto.getAccountStatus().equals(XtradeConstrants.USER_ACCOUNT_ACTIVE)) {
                   signupStatusMessage=messageSource.getMessage("userAccount.alreadyActivated",null,locale);
            }else if(accountVerificationStatusDto.getMobileVerificationStatus()==0){
                mobileOTPVerificationPending=true;
            }
        }catch(OTPMisMatchException e){
            log.warn("Email Address OTP Code Mis-Match",e);
           signupStatusMessage=messageSource.getMessage("userAccount.emailVerificationCodeMisMatch",null,locale);
        }catch(OTPAlreadyVerifiedException e){
            accountVerificationStatusDto=userAccountService.accountVerificationStatusDto(userAccountId);
            if(accountVerificationStatusDto.getAccountStatus().equals(XtradeConstrants.USER_ACCOUNT_ACTIVE)){
                signupStatusMessage=messageSource.getMessage("userAccount.alreadyActivated",null,locale);
            } else if (accountVerificationStatusDto.getMobileVerificationStatus()==0) {
                mobileOTPVerificationPending=true;
            }
        }

        if(mobileOTPVerificationPending){
            signupStatusMessage=messageSource.getMessage("userAccount.emailAddressVerified",null,locale);

            mobileOTPVerificationForm=new MobileOTPVerificationForm();
            mobileOTPVerificationForm.setUserAccountId(userAccountId);
            mobileOTPVerificationForm.setCustomerName(StringUtil.maskEmailAddress(accountVerificationStatusDto.getEmailAddress()));
            mobileOTPVerificationForm.setVerificationMobileNo(StringUtil.maskMobileNo(4,accountVerificationStatusDto.getMobileNo()));

            model.addAttribute("MobileOTPVerificationForm",mobileOTPVerificationForm);
            model.addAttribute("signupStatusMessage",signupStatusMessage);
            outcome= "customer-signup-otp-verification";
        }
        model.addAttribute("signupStatus",signupStatusMessage);
        return outcome;
    }

    @ExceptionHandler(UserAccountNotFoundException.class)
    public String handleUserAccountNotFoundException(UserAccountNotFoundException e,Model model,Locale locale){
        model.addAttribute("errorMessage",messageSource.getMessage("userAccount.notFound",null,locale));
        return "global-error";
    }

}
