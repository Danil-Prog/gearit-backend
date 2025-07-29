package com.gearit.api.repository;

import com.gearit.api.entity.notification.Notification;
import com.gearit.api.entity.notification.NotificationStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT notification FROM Notification AS notification WHERE notification.status = :status")
    List<Notification> getNotificationsByStatus(@Param("status") NotificationStatus status, Pageable pageable);
}
