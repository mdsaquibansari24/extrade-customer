package com.extrade.customer.security.service;

import com.extrade.customer.dto.UserAccountDto;
import com.extrade.customer.exception.UserAccountNotFoundException;
import com.extrade.customer.service.UserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountDto userAccountDto = null;
        UserDetails userDetails = null;
        try {
            userAccountDto = userAccountService.getUserByEmailAddress(username);
            userDetails = User.withUsername(userAccountDto.getEmailAddress())    //User is implementation class of UserDetails interface
                    .password(userAccountDto.getPassword())                                             //.withUsername is a method of User class with return type "UserBuilder"
                    .roles(userAccountDto.getRoleCode())                  //"UserBuilder" is a class provided by Spring Security for constructing instances of the User class
                    .accountExpired(false)
                    .accountLocked(userAccountDto.getStatus().equals("R"))
                    .disabled(userAccountDto.getStatus().equals("D"))
                    .credentialsExpired(false)
                    .credentialsExpired(false)
                    .passwordEncoder((password) -> {
                        return passwordEncoder.encode(password);
                    })
                    .authorities(userAccountDto.getRoleCode())
                    .build();
        } catch (UserAccountNotFoundException e) {
            log.error("user not found while authenticating", e);
            throw new UsernameNotFoundException("user with :" + username + "not found");
        }
        return userDetails;

    }
}
//The result of User.withUsername(userAccountDto.getEmailAddress()) is a special object
// called "UserBuilder"
//This UserBuilder holds the initial configuration for creating a user.
//Method Chaining:
//     1.The UserBuilder allows for method chaining, which means we can call multiple
//       methods on it one after another.
//     2.Each method call modifies the UserBuilder's configuration and returns the same
//       UserBuilder instance.












