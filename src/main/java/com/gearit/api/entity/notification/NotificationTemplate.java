package com.gearit.api.entity.notification;

import lombok.Getter;

@Getter
public enum NotificationTemplate {

    USER_CONFIRMED(
            "[Gearit] Подтверждения аккаунта.",
            "classpath:email/verify_body.html"
    ),

    PASSWORD_RECOVERED(
            "[Gearit] Восстановление пароля.",
            "classpath:email/password_recovery.html"
    )
    ;

    private final String subject;
    private final String filePath;

    NotificationTemplate(String subject, String filePath) {
        this.subject = subject;
        this.filePath = filePath;
    }
}
