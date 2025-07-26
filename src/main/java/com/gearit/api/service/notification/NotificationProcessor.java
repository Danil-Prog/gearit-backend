package com.gearit.api.service.notification;

import com.gearit.api.config.properties.*;
import com.gearit.api.entity.notification.*;
import com.resend.*;
import com.resend.core.exception.*;
import com.resend.services.emails.model.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class NotificationProcessor {

    private final Resend resend;
    private final EmailProperties emailProperties;
    private final NotificationService notificationService;
    private final NotificationTemplateEngine templateEngine;

    private final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);

    @Autowired
    public NotificationProcessor(
            Resend resend,
            EmailProperties emailProperties,
            NotificationService notificationService,
            NotificationTemplateEngine templateEngine
    ) {
        this.resend = resend;
        this.emailProperties = emailProperties;
        this.notificationService = notificationService;
        this.templateEngine = templateEngine;
    }

    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationService.getNotificationsByStatus(NotificationStatus.PENDING);
        List<Notification> sentNotifications = new ArrayList<>();

        pendingNotifications.forEach(notification -> {
            try {
                String to = notification.getSentToUser().getEmail();
                String template = templateEngine.getTemplateByType(notification.getTemplate());
                String subject = notification.getTemplate().getSubject();

                sendNotificationOrThrow(to, subject, template);

                notification.setStatus(NotificationStatus.SENT);
                sentNotifications.add(notification);
            } catch (ResendException e) {
                notification.setStatus(NotificationStatus.FAILED);
                logger.error("Error sending email, message: {}", e.getMessage());
            }
        });

        notificationService.updateNotifications(sentNotifications);
    }

    private void sendNotificationOrThrow(String to, String subject, String body) throws ResendException {
        String from = emailProperties.getFrom();
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        CreateEmailResponse response = resend.emails().send(emailOptions);
        logger.info("Email sent successfully to address: {}, response id: {}", to, response.getId());
    }
}
