package com.extrade.customer.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CustomerSignupForm {
    @Email
    @NotBlank
    private String emailAddress;

    @Pattern(regexp = "^(\\+\\d{1,2})?\\d{10}$")
    private String mobileNo;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$")
    private String password;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$")
    private String rePassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Past
    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy")

    private LocalDate dob;
    @NotBlank
    private String gender;
    @NotBlank
    private String termAndCondition;


}
