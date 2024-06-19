package com.mycode.pathpilotserver.email.web;

import com.mycode.pathpilotserver.email.models.Email;
import com.mycode.pathpilotserver.email.services.EmailServiceCommandImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/email")
@Slf4j
public class EmailController {

    private final EmailServiceCommandImpl emailServiceCommand;

    public EmailController(EmailServiceCommandImpl emailServiceCommand) {
        this.emailServiceCommand = emailServiceCommand;
    }

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
