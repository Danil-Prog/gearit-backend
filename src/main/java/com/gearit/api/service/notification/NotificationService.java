package com.gearit.api.service.notification;

import com.gearit.api.config.properties.EmailProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
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

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final String HTML_BODY;
    private static final String SUBJECT = "Пожалуйста, подтвердите регистрацию.";

    @Autowired
    public NotificationService(
            Resend resend,
            EmailProperties emailProperties,
            ResourceLoader resourceLoader
    ) throws IOException {
        this.resend = resend;
        this.emailProperties = emailProperties;
        this.resourceLoader = resourceLoader;

        this.HTML_BODY = loadHtmlFromResource();

        System.out.println(HTML_BODY);
    }

    public void sendConfirmEmail(String to, String code) {
        var bodyMessage = HTML_BODY.replace("{{ URL }}", emailProperties.getUrlVerify() + code);
        System.out.println(bodyMessage
        );
        sendEmail(to, bodyMessage);
    }

    private void sendEmail(String to, String body) {
        String from = emailProperties.getFrom();
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(NotificationService.SUBJECT)
                .html(body)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(emailOptions);
            logger.info("Email sent successfully to address: {}, response id: {}", to, response.getId());
        } catch (ResendException e) {
            logger.error("Error sending email, message: {}", e.getMessage());
        }
    }

    private String loadHtmlFromResource() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:email/verify_body.html");
        return Files.readString(resource.getFile().toPath());
    }
}
