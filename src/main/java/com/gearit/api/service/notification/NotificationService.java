package com.gearit.api.service.notification;

import com.gearit.api.config.properties.EmailProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Resend resend;
    private final EmailProperties emailProperties;

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(Resend resend, EmailProperties emailProperties) {
        this.resend = resend;
        this.emailProperties = emailProperties;
    }

    public void sendNotificationToEmail() {
        String to = "9mlcduov5oij@mail.ru";
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject("Gearit mail")
                .html("<p>Congrats on sending your <strong>first email</strong>!</p>")
                .build();
    }

    public void sendConfirmEmail(String to, String code) {
        String subject = "Please verify your registration";
        String body = "<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you<br>"
                + "Gearit.";

        body = body.replace("[[URL]]", "http://localhost:8080/api/v1/auth/verify?code=" + code);
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        String from = emailProperties.getFrom();
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(emailOptions);
            logger.info("Email sent successfully user: {}, response id: {}", to, response.getId());
        } catch (ResendException e) {
            logger.error("Error sending email, message: {}", e.getMessage());
        }
    }
}
