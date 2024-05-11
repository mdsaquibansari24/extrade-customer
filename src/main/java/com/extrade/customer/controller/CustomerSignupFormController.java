package com.extrade.customer.controller;

import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.service.UserAccountService;
import com.extrade.customer.validator.CustomerSignupFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer")
public class CustomerSignupFormController {
    private final CustomerSignupFormValidator customerSignupFormValidator;
    private final UserAccountService userAccountService;

    //This code is saying,
    // "Hey Spring, whenever you're converting form data into Java objects(customerSignupForm object),
    // I want you to use this customerSignupFormValidator to make sure the data is valid."
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.addValidators(customerSignupFormValidator);
    }

    @GetMapping("/signup")
    public String showCustomerSignupForm(Model model){
        model.addAttribute("customerSignupForm",new CustomerSignupForm());
        return "register-customer";
    }
    @PostMapping("/signup")
    public String doSignup(@ModelAttribute("customerSignupForm") @Valid CustomerSignupForm customerSignupForm, BindingResult errors, Model model){
        if(errors.hasErrors()){
            return "register-customer";
        }
        return "customer-signup-mobile-verification";
    }

}
