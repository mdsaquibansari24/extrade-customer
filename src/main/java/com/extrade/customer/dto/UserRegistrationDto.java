package com.extrade.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String emailAddress;
    private String password;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate dob;
    private String gender;
}
