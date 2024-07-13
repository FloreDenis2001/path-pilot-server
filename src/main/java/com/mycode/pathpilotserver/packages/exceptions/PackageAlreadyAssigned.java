package com.mycode.pathpilotserver.packages.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.CONFLICT)

public class PackageAlreadyAssigned extends RuntimeException{
    public PackageAlreadyAssigned(String message){
        super(message);
    }
}
