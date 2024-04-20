package com.mycode.pathpilotserver.email.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceCommandImpl implements EmailServiceCommand{

    private final JavaMailSender mailSender;

    public EmailServiceCommandImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("pathpilot116@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText("http://localhost:3000/drivers/add");
        this.mailSender.send(message);
    }


}
