package com.gearit.api.scheduler;

import com.gearit.api.service.notification.NotificationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationSenderScheduler implements JobScheduler {

    private final NotificationProcessor notificationProcessor;

    private final Logger logger = LoggerFactory.getLogger(NotificationSenderScheduler.class);

    @Autowired
    public NotificationSenderScheduler(NotificationProcessor notificationProcessor) {
        this.notificationProcessor = notificationProcessor;
    }

    /**
     * Периодическая рассылка уведомлений, каждые 5 секунд.
     */
    @Override
    @Scheduled(fixedRate = 5000)
    public void schedule() {
        try {
            notificationProcessor.processPendingNotifications();
        } catch (Exception e) {
            logger.error("Error processing pending notifications: {}", e.getMessage());
        }
    }
}
