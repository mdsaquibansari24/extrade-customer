package com.extrade.customer.exception;

public class OTPMisMatchException extends UserAccountServiceException{
    public OTPMisMatchException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
