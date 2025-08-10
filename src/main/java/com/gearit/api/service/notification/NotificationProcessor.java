package com.gearit.api.service.notification;

import com.gearit.api.config.properties.CorsProperties;
import com.gearit.api.config.properties.EmailProperties;
import com.gearit.api.entity.notification.Notification;
import com.gearit.api.entity.notification.NotificationStatus;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.gearit.api.entity.notification.NotificationStatus.PENDING;

@Component
public class NotificationProcessor {

    private final Resend resend;
    private final EmailProperties emailProperties;
    private final NotificationService notificationService;
    private final NotificationTemplateEngine templateEngine;
    private final CorsProperties corsProperties;

    private final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);

    @Autowired
    public NotificationProcessor(
            Resend resend,
            EmailProperties emailProperties,
            NotificationService notificationService,
            NotificationTemplateEngine templateEngine,
            CorsProperties corsProperties
    ) {
        this.resend = resend;
        this.emailProperties = emailProperties;
        this.notificationService = notificationService;
        this.templateEngine = templateEngine;
        this.corsProperties = corsProperties;
    }

    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationService.getNotificationsByStatus(PENDING);
        List<Notification> processedNotifications = new ArrayList<>();

        pendingNotifications.forEach(notification -> {
            try {
                String to = notification.getSentToUser().getEmail();
                String template = templateEngine.renderTemplate(
                        notification.getTemplate(),
                        notification.getVariables()
                );
                String subject = notification.getTemplate().getSubject();

                sendNotificationOrThrow(to, subject, template);

                notification.setStatus(NotificationStatus.SENT);
            } catch (Exception e) {
                notification.setStatus(NotificationStatus.FAILED);
                logger.error("Error sending email, message: {}", e.getMessage());
            } finally {
                processedNotifications.add(notification);
            }
        });

        notificationService.updateNotifications(processedNotifications);
    }

    private void sendNotificationOrThrow(String to, String subject, String body) throws ResendException {
        String from = emailProperties.getFrom();
        String url = corsProperties.getCorsAllowedOriginsUri();
        body = body.replace("{{ URL }}", url);

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
