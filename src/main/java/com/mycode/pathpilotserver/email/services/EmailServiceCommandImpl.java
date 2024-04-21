package com.mycode.pathpilotserver.email.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class EmailServiceCommandImpl implements EmailServiceCommand{

    private final JavaMailSender mailSender;

    private final Map<String, LocalDateTime> linkExpirationMap = new HashMap<>();


    public EmailServiceCommandImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to) {
        String link = generateUniqueLink();
        String subject = "Your Driver Creation Link";
        String body = "Click here to create a Driver: " + link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("pathpilot116@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    @Override
    public boolean isLinkValid(String code) {
        LocalDateTime expirationTime = linkExpirationMap.get(code);
        return expirationTime != null && expirationTime.isAfter(LocalDateTime.now());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredLinks() {
        linkExpirationMap.entrySet().removeIf(entry -> entry.getValue().isBefore(LocalDateTime.now()));
    }

    private String generateUniqueLink() {
        String uniqueCode = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1);
        long expirationTimestamp = expirationTime.toEpochSecond(ZoneOffset.UTC);
        linkExpirationMap.put(uniqueCode, expirationTime);
        return "http://localhost:3000/drivers/add?code=" + uniqueCode + "&expires=" + expirationTimestamp;
    }

    @Override
    public void removeLinkAfterCreation(String code) {
        linkExpirationMap.remove(code);
    }



}
