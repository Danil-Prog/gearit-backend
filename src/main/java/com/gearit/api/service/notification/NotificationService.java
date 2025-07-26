package com.gearit.api.service.notification;

import com.gearit.api.entity.notification.*;
import com.gearit.api.entity.user.*;
import com.gearit.api.repository.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(
            NotificationTemplate template,
            UserProvider userProvider,
            Map<String, Object> variable
    ) {
        Notification notification = new Notification();
        notification.setType(NotificationType.EMAIL);
        notification.setTemplate(template);
        notification.setSentToUser(userProvider);
        notification.setVariables(variable);

        notificationRepository.save(notification);
    }

    public void updateNotificationStatus(Long id, NotificationStatus status) {
        var notification = notificationRepository.findById(id).orElseThrow();
        notification.setStatus(status);
        notificationRepository.save(notification);
    }

    public void updateNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    public List<Notification> getNotificationsByStatus(NotificationStatus notificationStatus) {
        return notificationRepository.getNotificationsByStatus(notificationStatus);
    }
}
