package com.mycode.pathpilotserver.intercom.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/maps")
public class DirectionsController {

    @Autowired
    private DirectionsService directionsService;

    @GetMapping("/directions")
    public DirectionsResult getDirections(@RequestParam String origin, @RequestParam String destination) {
        try {
            return directionsService.getDirections(origin, destination);
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
