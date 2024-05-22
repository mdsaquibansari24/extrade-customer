package com.extrade.customer.form;

import lombok.Data;

@Data
public class MobileOTPVerificationForm {
    private Long userAccountId;
    private String customerName;
    private String verificationMobileNo;
    private String verificationEmailAddress;
    private String otpCode;

}
//register-customer.html form ke baad ,ek aur form open hoga jise hm MobileOTPVerificationForm ke naam se bana rahe
//