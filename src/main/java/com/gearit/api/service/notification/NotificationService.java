package com.gearit.api.service.notification;

import com.gearit.api.config.properties.EmailProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Resend resend;
    private final EmailProperties emailProperties;
    private final ResourceLoader resourceLoader;
    private String htmlBody;

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private static final String SUBJECT = "Пожалуйста, подтвердите регистрацию.";
    private static final String HTML_LOCATION = "classpath:email/verify_body.html";

    @Autowired
    public NotificationService(
            Resend resend,
            EmailProperties emailProperties,
            ResourceLoader resourceLoader
    ) {
        this.resend = resend;
        this.emailProperties = emailProperties;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            logger.info("Start initialize HTML body for notification from resource");

            htmlBody = loadHtmlFromResource();

            logger.info("Finish initialize HTML body for notification from resource, html length: {}", htmlBody.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendConfirmEmail(String to, String code) {
        var bodyMessage = htmlBody.replace("{{ URL }}", emailProperties.getUrlVerify() + code);
        sendEmail(to, bodyMessage);
    }

    private void sendEmail(String to, String body) {
        try {
            String from = emailProperties.getFrom();
            CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                    .from(from)
                    .to(to)
                    .subject(NotificationService.SUBJECT)
                    .html(body)
                    .build();

            CreateEmailResponse response = resend.emails().send(emailOptions);
            logger.info("Email sent successfully to address: {}, response id: {}", to, response.getId());
        } catch (ResendException e) {
            logger.error("Error sending email, message: {}", e.getMessage());
        }
    }

    private String loadHtmlFromResource() throws IOException {
        Resource resource = resourceLoader.getResource(HTML_LOCATION);
        return Files.readString(resource.getFile().toPath());
    }
}
