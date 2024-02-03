package com.mycode.pathpilotserver.shipmentDetails.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShipmentDetailsNotFoundException extends RuntimeException {
    public ShipmentDetailsNotFoundException(String message) {
        super(message);
    }
}
