package com.mycode.pathpilotserver.customers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class CustomerAlreadyExist extends RuntimeException{
    public CustomerAlreadyExist(String message) {
        super(message);
    }
}
