package com.extrade.customer.exception;

public class OTPAlreadyVerifiedException extends UserAccountServiceException{
    public OTPAlreadyVerifiedException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
