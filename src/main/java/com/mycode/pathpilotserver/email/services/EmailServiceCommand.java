package com.mycode.pathpilotserver.email.services;

public interface EmailServiceCommand {

    void sendEmail(String to,String companyRegistrationNumber);




    void resetPassword(String email);

}
