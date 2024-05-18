package com.mycode.pathpilotserver.packages.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String message) {
        super(message);
    }
}
