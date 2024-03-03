package com.mycode.pathpilotserver.driver.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DriverAlreadyExistException extends RuntimeException{
    public DriverAlreadyExistException(String message) {
        super(message);
    }
}
