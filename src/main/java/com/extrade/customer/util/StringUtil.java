package com.extrade.customer.util;

import org.bouncycastle.crypto.prng.RandomGenerator;

import java.security.SecureRandom;

public class StringUtil {
    public static String maskMobileNo(int len,String mobileNo){
        String temp="";
        for(int i=0;i<(mobileNo.length()-len);i++){

             temp=temp+"x";
        }
       return temp+mobileNo.substring(mobileNo.length()-len,mobileNo.length());
    }

    public static String maskEmailAddress(String emailAddress){
        String domain=emailAddress.substring(emailAddress.indexOf('@'),emailAddress.length());
        String name=emailAddress.substring(0,emailAddress.indexOf('@'));
        String maskedPart="";
        SecureRandom random=new SecureRandom();
        int randomLength=random.nextInt(name.length());
        for(int i=0;i<randomLength;i++){
            maskedPart=maskedPart+"x";
        }

        return maskedPart+name.substring(randomLength,name.length())+domain;
    }
}
