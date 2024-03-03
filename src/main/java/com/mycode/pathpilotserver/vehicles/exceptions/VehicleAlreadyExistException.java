package com.mycode.pathpilotserver.vehicles.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class VehicleAlreadyExistException extends RuntimeException{
    public VehicleAlreadyExistException(String message) {
        super(message);
    }
}
