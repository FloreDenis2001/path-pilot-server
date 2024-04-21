package com.mycode.pathpilotserver.email.services;

import java.time.LocalDateTime;

public interface EmailServiceCommand {

    void sendEmail(String to);

    boolean isLinkValid(String code);

    void removeExpiredLinks();

    void removeLinkAfterCreation(String code);
}
