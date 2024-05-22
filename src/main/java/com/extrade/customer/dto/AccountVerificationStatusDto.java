package com.extrade.customer.dto;

import lombok.Data;

@Data
public class AccountVerificationStatusDto {
    private Long userAccountId;
    private int mobileVerificationStatus;
    private int emailVerificationStatus;
    private String accountStatus;
    private String mobileNo;
    private String emailAddress;
}
