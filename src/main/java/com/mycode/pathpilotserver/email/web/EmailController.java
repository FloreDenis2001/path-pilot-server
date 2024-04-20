package com.mycode.pathpilotserver.email.web;

import com.mycode.pathpilotserver.email.models.Email;
import com.mycode.pathpilotserver.email.services.EmailServiceCommand;
import com.mycode.pathpilotserver.email.services.EmailServiceCommandImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailServiceCommandImpl emailServiceCommand;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {
        emailServiceCommand.sendEmail(email.getTo(), email.getSubject(), email.getBody());
        return ResponseEntity.ok("Email sent");
    }


}
