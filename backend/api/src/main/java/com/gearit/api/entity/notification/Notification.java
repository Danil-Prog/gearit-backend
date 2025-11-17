package com.gearit.api.entity.notification;

import com.gearit.api.entity.user.UserProvider;
import com.gearit.common.constants.TableNames;
import com.gearit.common.converter.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Map;

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
