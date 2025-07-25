package com.gearit.api.entity.notification;

import com.gearit.api.constants.TableNames;
import com.gearit.api.entity.user.UserProvider;
import jakarta.persistence.*;

@Entity
@Table(name = TableNames.NOTIFICATIONS)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "template")
    private NotificationTemplate template;

    @Column(name = "type")
    private NotificationType type;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @OneToOne(fetch = FetchType.EAGER)
    private UserProvider sentToUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public UserProvider getSentToUser() {
        return sentToUser;
    }

    public void setSentToUser(UserProvider sentToUser) {
        this.sentToUser = sentToUser;
    }
}
