package com.extrade.customer.exception;

public class UserAccountServiceException extends RuntimeException{
    //this is a basic exception class
    private String errorCode;
    private String errorMessage;

    public UserAccountServiceException(String errorMessage, String errorCode) {

        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public UserAccountServiceException(Throwable cause, String errorCode, String errorMessage) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
//for each type of error ,create on exception class