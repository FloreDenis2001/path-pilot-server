package com.mycode.pathpilotserver.email.services;

import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmailServiceCommandImpl implements EmailServiceCommand{

    private final JavaMailSender mailSender;

    private final UserRepo userRepo;

    static final Map<String, LocalDateTime> linkExpirationMap = new HashMap<>();


    public EmailServiceCommandImpl(JavaMailSender mailSender, UserRepo userRepo) {
        this.mailSender = mailSender;
        this.userRepo = userRepo;
    }

    @Override
    public void sendEmail(String to,String companyRegistrationNumber) {
        String link = generateUniqueLink("createDriver",companyRegistrationNumber);
        String subject = "Your Driver Creation Link";
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("pathpilot116@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>PathPilot</title>"
                    + "</head>"
                    + "<body>"
                    + "<div style=\"text-align: center;\">"
                    + "<h2>Welcome to PathPilot</h2>"
                    + "<p>You've been invited to create a driver account.</p>"
                    + "<p>Please click the button below to create your account:</p>"
                    + "<a href=\"" + link + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px;\">Create Account</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
        };
        mailSender.send(preparator);
    }


    public static boolean isLinkValid(String code) {
        LocalDateTime expirationTime = linkExpirationMap.get(code);
        return expirationTime != null && expirationTime.isAfter(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredLinks() {
        linkExpirationMap.entrySet().removeIf(entry -> entry.getValue().isBefore(LocalDateTime.now()));
    }

    String generateUniqueLink(String linkType, String identifier) {
        String uniqueCode = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1);
        long expirationTimestamp = expirationTime.toEpochSecond(ZoneOffset.UTC);
        linkExpirationMap.put(uniqueCode, expirationTime);

        String path = "";
        if (linkType.equals("createDriver")) {
            path = "/drivers/add";
        } else if (linkType.equals("resetPassword")) {
            path = "/reset-password";
        }

        return "http://3.253.99.13" + path + "?code=" + uniqueCode + "&expires=" + expirationTimestamp + "&identifier=" + identifier;
    }

    public static void removeLinkAfterCreation(String code) {
        linkExpirationMap.remove(code);
    }

    @Override
    public void resetPassword(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for email: " + email);
        }
        
        String link = generateUniqueLink("resetPassword",email);
        String subject = "Reset Password";

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("pathpilot116@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>PathPilot</title>"
                    + "</head>"
                    + "<body>"
                    + "<div style=\"text-align: center;\">"
                    + "<h2>Password Reset</h2>"
                    + "<p>You've requested to reset your password.</p>"
                    + "<p>Please click the button below to reset your password:</p>"
                    + "<a href=\"" + link + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px;\">Reset Password</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
        };

        mailSender.send(preparator);
    }




}
