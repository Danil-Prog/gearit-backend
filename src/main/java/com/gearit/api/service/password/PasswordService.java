package com.gearit.api.service.password;

import com.gearit.api.entity.user.*;
import com.gearit.api.service.passwordrecovery.*;
import com.gearit.api.service.user.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class PasswordService {

    private final UserProviderService userProviderService;
    private final PasswordRecoveryService passwordRecoveryService;

    @Autowired
    public PasswordService(
            UserProviderService userProviderService,
            PasswordRecoveryService passwordRecoveryService
    ) {
        this.userProviderService = userProviderService;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    /**
     * Отправляет пользователю на почту уведомление с ссылкой на восстановление пароля
     *
     * @param email - адрес электронной почты пользователя
     */
    public void createAndSendRecoveryPasswordNotification(String email) {
        UserProvider userProvider = userProviderService.getUserProviderByEmailOrThrow(email);
        passwordRecoveryService.createNewPasswordRecovery(userProvider.getId());
    }

}
