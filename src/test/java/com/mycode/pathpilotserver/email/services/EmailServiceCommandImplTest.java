package com.mycode.pathpilotserver.email.services;

import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceCommandImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private EmailServiceCommandImpl emailServiceCommand;


    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String companyRegistrationNumber = "1234567890";

        String link = "http://localhost:3000/drivers/add?code=mockCode&expires=1234567890&identifier=" + companyRegistrationNumber;
        EmailServiceCommandImpl spyService = spy(emailServiceCommand);
        doReturn(link).when(spyService).generateUniqueLink(anyString(), anyString());

        spyService.sendEmail(to, companyRegistrationNumber);

        verify(mailSender, times(1)).send(any(MimeMessagePreparator.class));
    }

    @Test
    void testResetPassword() {
        String email = "user@example.com";
        User user = new Customer();
        user.setEmail(email);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        String link = "http://localhost:3000/reset-password?code=mockCode&expires=1234567890&identifier=" + email;
        EmailServiceCommandImpl spyService = spy(emailServiceCommand);
        doReturn(link).when(spyService).generateUniqueLink(anyString(), anyString());

        spyService.resetPassword(email);

        verify(mailSender, times(1)).send(any(MimeMessagePreparator.class));
    }

    @Test
    void testResetPasswordUserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> emailServiceCommand.resetPassword(email));
    }

    @Test
    void testIsLinkValid() {
        String code = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1);
        EmailServiceCommandImpl.linkExpirationMap.put(code, expirationTime);

        boolean isValid = EmailServiceCommandImpl.isLinkValid(code);

        assertTrue(isValid);
    }

    @Test
    void testIsLinkInvalid() {
        String code = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(1);
        EmailServiceCommandImpl.linkExpirationMap.put(code, expirationTime);

        boolean isValid = EmailServiceCommandImpl.isLinkValid(code);

        assertFalse(isValid);
    }

    @Test
    void testRemoveExpiredLinks() {
        String expiredCode = UUID.randomUUID().toString();
        EmailServiceCommandImpl.linkExpirationMap.put(expiredCode, LocalDateTime.now().minusDays(1));

        EmailServiceCommandImpl emailService = new EmailServiceCommandImpl(mailSender, userRepo);
        emailService.removeExpiredLinks();

        assertNull(EmailServiceCommandImpl.linkExpirationMap.get(expiredCode));
    }

    @Test
    void testRemoveLinkAfterCreation() {
        String code = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1);
        EmailServiceCommandImpl.linkExpirationMap.put(code, expirationTime);

        EmailServiceCommandImpl.removeLinkAfterCreation(code);

        assertNull(EmailServiceCommandImpl.linkExpirationMap.get(code));
    }

    @Test
    void testGenerateUniqueLinkForCreateDriver() {
        String companyRegistrationNumber = "1234567890";
        String link = emailServiceCommand.generateUniqueLink("createDriver", companyRegistrationNumber);

        assertTrue(link.contains("/drivers/add"));
        assertTrue(link.contains("code="));
        assertTrue(link.contains("expires="));
        assertTrue(link.contains("identifier=" + companyRegistrationNumber));
    }

    @Test
    void testGenerateUniqueLinkForResetPassword() {
        String email = "user@example.com";
        String link = emailServiceCommand.generateUniqueLink("resetPassword", email);

        assertTrue(link.contains("/reset-password"));
        assertTrue(link.contains("code="));
        assertTrue(link.contains("expires="));
        assertTrue(link.contains("identifier=" + email));
    }

    @Test
    void testGenerateUniqueLinkWithDifferentTypes() {
        String companyRegistrationNumber = "1234567890";
        String linkForCreateDriver = emailServiceCommand.generateUniqueLink("createDriver", companyRegistrationNumber);
        String linkForResetPassword = emailServiceCommand.generateUniqueLink("resetPassword", companyRegistrationNumber);

        assertNotEquals(linkForCreateDriver, linkForResetPassword);
    }
}
