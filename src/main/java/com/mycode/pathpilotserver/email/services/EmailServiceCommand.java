package com.mycode.pathpilotserver.email.services;

import java.time.LocalDateTime;

public interface EmailServiceCommand {

    void sendEmail(String to,String companyRegistrationNumber);




    void resetPassword(String email);

}
