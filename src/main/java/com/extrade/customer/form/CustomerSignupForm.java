package com.extrade.customer.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class CustomerSignupForm {
    @Email
    @NotBlank
    private String emailAddress;
    @NotBlank
    private String mobileNo;
    @Length(min = 8, max =20)
    private String password;
    @Length(min = 8 , max = 20)
    private String rePassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Past

    private LocalDate dob;
    @NotBlank
    private String gender;
    @NotBlank
    private String termAndCondition;


}
