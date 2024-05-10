package com.extrade.customer.controller;

import com.extrade.customer.form.CustomerSignupForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerSignupFormController {

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
