package com.extrade.customer.exception;

public class UserAccountNotFoundException extends UserAccountServiceException{
    public UserAccountNotFoundException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
