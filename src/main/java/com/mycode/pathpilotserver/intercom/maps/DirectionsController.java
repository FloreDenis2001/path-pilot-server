package com.mycode.pathpilotserver.intercom.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/maps")
@Slf4j
@CrossOrigin
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