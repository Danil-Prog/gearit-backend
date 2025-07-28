package com.gearit.api.service.password;

import com.gearit.api.entity.notification.NotificationTemplate;
import com.gearit.api.entity.user.PasswordRecovery;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.service.notification.NotificationService;
import com.gearit.api.service.passwordrecovery.PasswordRecoveryService;
import com.gearit.api.service.user.UserProviderService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final UserProviderService userProviderService;
    private final PasswordRecoveryService passwordRecoveryService;
    private final NotificationService notificationService;

    private final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    @Autowired
    public PasswordService(
            UserProviderService userProviderService,
            PasswordRecoveryService passwordRecoveryService,
            NotificationService notificationService
    ) {
        this.userProviderService = userProviderService;
        this.passwordRecoveryService = passwordRecoveryService;
        this.notificationService = notificationService;
    }

    /**
     * Отправляет пользователю на почту уведомление со ссылкой на восстановление пароля
     *
     * @param email - адрес электронной почты пользователя
     */
    public void forgot(String email) {
        UserProvider userProvider = userProviderService.getUserProviderByEmailOrThrow(email);
        var passwordRecovery = passwordRecoveryService.createPasswordRecovery(userProvider.getId());

        notificationService.createNotification(
                NotificationTemplate.PASSWORD_RECOVERED,
                userProvider,
                Map.of("code", passwordRecovery.getCode())
        );

        logger.info("Password recovery code: {}, sent to user with email: {}", passwordRecovery.getCode(), email);
    }

    public PasswordRecovery verifyCode(String code) {
        var passwordRecovery = passwordRecoveryService.getPasswordRecoveryByCode(code);
        if (passwordRecovery == null) {
            throw new WebClientException("Password user recovery failed", "Recovery code sent is invalid.");
        }

        return passwordRecovery;
    }

    public void resetPassword(String code, String newPassword) {
        var passwordRecovery = verifyCode(code);
        Long userId = passwordRecovery.getUserProviderId();

        userProviderService.updateUserProviderPassword(userId, newPassword);
    }
}
