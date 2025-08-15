package com.gearit.api.entity.notification;

import com.gearit.api.constants.*;
import com.gearit.api.converter.*;
import com.gearit.api.entity.user.*;
import jakarta.persistence.*;
import java.util.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = TableNames.NOTIFICATIONS)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template")
    @Enumerated(EnumType.STRING)
    private NotificationTemplate template;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.PENDING;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_provider_id", referencedColumnName = "id")
    private UserProvider sentToUser;

    @Convert(converter = JsonConverter.class)
    @Column(name = "variables", columnDefinition = "JSONB")
    private Map<String, Object> variables;
}
