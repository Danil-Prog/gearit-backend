package com.gearit.api.service.notification;

import com.gearit.api.entity.notification.Notification;
import com.gearit.api.entity.notification.NotificationStatus;
import com.gearit.api.entity.notification.NotificationTemplate;
import com.gearit.api.entity.notification.NotificationType;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.NotificationRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
            Map<String, Object> variables
    ) {
        Notification notification = new Notification();
        notification.setType(NotificationType.EMAIL);
        notification.setTemplate(template);
        notification.setSentToUser(userProvider);
        notification.setVariables(variables);

        notificationRepository.save(notification);
    }

    public void updateNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    public List<Notification> getNotificationsByStatus(NotificationStatus notificationStatus) {
        return notificationRepository.getNotificationsByStatus(notificationStatus, PageRequest.of(0, 10));
    }
}
