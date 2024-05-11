package com.extrade.customer.validator;

import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@RequiredArgsConstructor
public class CustomerSignupFormValidator implements Validator {
    private final UserAccountService userAccountService;
    @Override
    //if a particular class supports another class.
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CustomerSignupForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //search about it on chatgpt
        CustomerSignupForm signupForm=null;
        signupForm=(CustomerSignupForm) target;

        if(errors.hasFieldErrors("password")==false && (errors.hasFieldErrors("rePassword")==false)){
            if(signupForm.getPassword().equals(signupForm.getRePassword())==false){
                errors.rejectValue("rePassword","rePassword.mismatch");//lets add this error in error.properties
            }
        }
        //if emailAddress is valid at client side then check email validation at server side
         if(errors.hasFieldErrors("emailAddress")==false){
             long cEmail= userAccountService.countUserAccountByEmailService(signupForm.getEmailAddress());
             if(cEmail>0){
                 errors.rejectValue("emailAddress","emailAddress.notAvailable"); //lets add this eror in erors.properties
             }
         }

         //if MobileNo is valid at client side then we will go to check at server side
         if(errors.hasFieldErrors("mobileNo")==false){
             long cMobile= userAccountService.countUserAccountByMobileNo(signupForm.getMobileNo());
             if(cMobile>0){
                 errors.rejectValue("mobileNo","mobileNo.notAvailable");
             }

        }


    }
}
//now inject this class into the controller