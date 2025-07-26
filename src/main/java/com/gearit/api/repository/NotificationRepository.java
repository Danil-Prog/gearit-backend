package com.gearit.api.repository;

import com.gearit.api.entity.notification.*;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT notification FROM Notification AS notification WHERE notification.status = :status")
    List<Notification> getNotificationsByStatus(@Param("status") NotificationStatus status, Pageable pageable);
}
