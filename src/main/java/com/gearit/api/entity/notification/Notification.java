package com.gearit.api.entity.notification;

import com.gearit.api.constants.*;
import com.gearit.api.converter.*;
import com.gearit.api.entity.user.*;
import jakarta.persistence.*;
import java.util.*;

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
    @MapsId
    @JoinColumn(name = "user_provider_id", referencedColumnName = "id")
    private UserProvider sentToUser;

    @Convert(converter = JsonConverter.class)
    @Column(name = "variables", columnDefinition = "JSONB")
    private Map<String, Object> variables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplate template) {
        this.template = template;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public UserProvider getSentToUser() {
        return sentToUser;
    }

    public void setSentToUser(UserProvider sentToUser) {
        this.sentToUser = sentToUser;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
