package com.mycode.pathpilotserver.email.web;

import com.mycode.pathpilotserver.email.models.Email;
import com.mycode.pathpilotserver.email.services.EmailServiceCommand;
import com.mycode.pathpilotserver.email.services.EmailServiceCommandImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailServiceCommandImpl emailServiceCommand;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Email email , @RequestParam String companyRegistrationNumber) {
        emailServiceCommand.sendEmail(email.getTo(),companyRegistrationNumber);
        return ResponseEntity.ok("Email sent");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        emailServiceCommand.resetPassword(email);
        return ResponseEntity.ok("Email sent");
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Boolean> validateLink(@PathVariable String code) {
        boolean isValid = EmailServiceCommandImpl.isLinkValid(code);
        return ResponseEntity.ok(isValid);
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity<String> removeLink(@PathVariable String code) {
        EmailServiceCommandImpl.removeLinkAfterCreation(code);
        return ResponseEntity.ok("Link removed");
    }


}
