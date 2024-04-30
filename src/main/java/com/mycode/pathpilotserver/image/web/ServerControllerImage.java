package com.mycode.pathpilotserver.image.web;

import com.mycode.pathpilotserver.image.services.ImageServiceQuerryImpl;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/v1/image")
@AllArgsConstructor
public class ServerControllerImage {

    private ImageServiceQuerryImpl imageServiceQuerryImpl;


    @GetMapping("/user/")
    public ResponseEntity<String> findImageByUserAfterEmail(@RequestParam("email") String email) {
        String base64Image = imageServiceQuerryImpl.findImageByUserAfterEmail(email);
        return ResponseEntity.ok(base64Image);
    }
}
