package com.extrade.customer.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ErrorMessage {
    //looking at the swagger documentation write this
    private String messageId;
    private String errorCode;
    private Date messageDateTime;
    private String errorMessage;
    private String originator;
}
