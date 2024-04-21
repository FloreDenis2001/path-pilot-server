package com.mycode.pathpilotserver.email.models;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class Email {
    private String to;
    private String subject;
    private String body;
}
