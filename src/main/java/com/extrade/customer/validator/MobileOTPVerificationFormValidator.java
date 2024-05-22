package com.extrade.customer.validator;

import com.extrade.customer.form.MobileOTPVerificationForm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MobileOTPVerificationFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MobileOTPVerificationForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MobileOTPVerificationForm mobileOTPVerificationForm=(MobileOTPVerificationForm) target;
        if(StringUtils.hasText(mobileOTPVerificationForm.getOtpCode())==false){
            errors.rejectValue("otpCode","otpCode.blank");
        } else if (mobileOTPVerificationForm.getOtpCode().trim().length()!=6) {
            errors.rejectValue("otpCode","otpCode.invalid");

        }
    }
}
