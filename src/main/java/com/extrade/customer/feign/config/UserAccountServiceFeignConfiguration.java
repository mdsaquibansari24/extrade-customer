package com.extrade.customer.feign.config;

import com.extrade.customer.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class UserAccountServiceFeignConfiguration {    //we are writing decoder
    //at client side we want to recieve the response genarated by postman as exception not as response when we write put request to verify mobile otp
          //http://localhost:8090/account/12/458796/VERIFY_MOBILE
          // "messageId": "jfkdf5454dfdfgds"
          //"errorCode": "user.NotFound"
          //   ...
          // for this we need to write feign Config

    @Bean
    public ErrorDecoder errorDecoder(){
        return new AccountServiceExceptionDecoder();
    }


    public final class AccountServiceExceptionDecoder implements ErrorDecoder{
         //now,we are making  this decoder as Bean
        @Override
        public Exception decode(String s, Response response) { //this decode method will be called when the proxy(feign interface) has recieved the response from rest api other than 200,which is an error,as part of error always we r returning errormesage object
            UserAccountServiceException userAccountServiceException=null;
            ObjectMapper objectMapper=new ObjectMapper();  //use objectMapper to extract responsebody into errorMessage object
            ErrorMessage errorMessage=null;
            Reader reader=null;

            try {
                reader = response.body().asReader(StandardCharsets.UTF_8);  //getting the response into reader object
                errorMessage = objectMapper.readValue(reader, ErrorMessage.class); //read value from reader and map to errorMessage
                //(response body we are feeding and converting into errorMessage)


            } catch (IOException e) {
                log.error("unknown error while parsing the error-response",e);
                throw new UserAccountServiceException(e,"unkown","error while parsing the error response");
            }
              //now we are de-coding the error response
              if(response.status()== HttpStatus.NOT_FOUND.value()){   //404
                  userAccountServiceException= new UserAccountNotFoundException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
                  
              } else if (response.status()==HttpStatus.GONE.value()) { //410
                  userAccountServiceException=new UserAccountAlreadyActivatedException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());

              } else if (response.status()==HttpStatus.BAD_REQUEST.value()) {  //400
                  userAccountServiceException=new OTPMisMatchException(errorMessage.getErrorCode(),errorMessage.getErrorMessage());

              } else if (response.status()==HttpStatus.UNPROCESSABLE_ENTITY.value()) { //422
                   userAccountServiceException=new OTPAlreadyVerifiedException(errorMessage.getErrorCode(),errorMessage.getErrorMessage());

              }  else {
                  //if error is not matching any above ,then we return base exception class
                  userAccountServiceException=new UserAccountServiceException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
              }


            return userAccountServiceException;
        }
    }

}


//finally: this one will be used when we r calling "UserAccountServive(Feign interface)" from controller