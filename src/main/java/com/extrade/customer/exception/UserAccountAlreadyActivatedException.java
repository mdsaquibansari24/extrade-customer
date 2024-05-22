package com.extrade.customer.exception;

public class UserAccountAlreadyActivatedException extends UserAccountServiceException{
    public UserAccountAlreadyActivatedException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
