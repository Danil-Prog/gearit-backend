package com.gearit.api.service.notification;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Resend resend;

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(Resend resend) {
        this.resend = resend;
    }

    public void sendNotificationToEmail() {
        String to = "9mlcduov5oij@mail.ru";
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject("Gearit mail")
                .html("<p>Congrats on sending your <strong>first email</strong>!</p>")
                .build();

//        try {
//            CreateEmailResponse response = resend.emails().send(emailOptions);
//            logger.info("Email sent successfully user: {}, response id: {}", to, response.getId());
//        } catch (ResendException e) {
//            logger.error("Error sending email, message: {}", e.getMessage());
//        }
    }
}
