package com.gearit.api.service.password;

import com.gearit.api.entity.notification.*;
import com.gearit.api.entity.user.*;
import com.gearit.api.service.notification.*;
import com.gearit.api.service.passwordrecovery.*;
import com.gearit.api.service.user.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

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
     * Отправляет пользователю на почту уведомление с ссылкой на восстановление пароля
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

}
