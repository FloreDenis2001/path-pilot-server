package com.mycode.pathpilotserver.user.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String message) {
        super(message);
    }
}
