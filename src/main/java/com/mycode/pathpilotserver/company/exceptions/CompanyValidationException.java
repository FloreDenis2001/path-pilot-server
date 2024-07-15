package com.mycode.pathpilotserver.company.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CompanyValidationException extends RuntimeException{
    public CompanyValidationException(String message) {
        super(message);
    }
}
