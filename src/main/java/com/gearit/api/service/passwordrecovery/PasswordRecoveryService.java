package com.gearit.api.service.passwordrecovery;

import com.gearit.api.entity.user.*;
import com.gearit.api.repository.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class PasswordRecoveryService {

    private final PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    public PasswordRecoveryService(PasswordRecoveryRepository passwordRecoveryRepository) {
        this.passwordRecoveryRepository = passwordRecoveryRepository;
    }

    public PasswordRecovery createPasswordRecovery(Long userProviderId) {
        var randomCode = UUID.randomUUID().toString();

        var passwordRecovery = new PasswordRecovery();
        passwordRecovery.setUserProviderId(userProviderId);
        passwordRecovery.setCode(randomCode);

        // Время жизни кода восстановления пароля 24 часа
        passwordRecovery.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

        return passwordRecoveryRepository.save(passwordRecovery);
    }

    public PasswordRecovery getPasswordRecoveryByCode(String code) {
        return passwordRecoveryRepository.findByCode(code).orElse(null);
    }
}
