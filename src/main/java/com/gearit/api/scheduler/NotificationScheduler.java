package com.gearit.api.scheduler;

import com.gearit.api.service.notification.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Component
public class NotificationScheduler {

    private final NotificationProcessor notificationProcessor;

    @Autowired
    public NotificationScheduler(NotificationProcessor notificationProcessor) {
        this.notificationProcessor = notificationProcessor;
    }

    @Scheduled(fixedRate = 5000)
    public void schedule() {
        notificationProcessor.processPendingNotifications();
    }
}
