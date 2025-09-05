package com.gearit.api.service.actioncode;

import com.gearit.api.entity.actioncode.ActionCode;
import com.gearit.api.entity.actioncode.ActionType;
import com.gearit.api.repository.ActionCodeRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ActionCodeService {

    private final ActionCodeRepository actionCodeRepository;

    public ActionCodeService(ActionCodeRepository actionCodeRepository) {
        this.actionCodeRepository = actionCodeRepository;
    }

    public ActionCode createCode(Long userProviderId, ActionType actionType) {
        var randomCode = UUID.randomUUID().toString();

        var actionCode = new ActionCode();
        actionCode.setUserProviderId(userProviderId);
        actionCode.setCode(randomCode);
        actionCode.setActionType(actionType);

        // Время жизни кода восстановления пароля 24 часа
        actionCode.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

        return actionCodeRepository.save(actionCode);
    }

    public ActionCode findByCode(String code) {
        return actionCodeRepository.findByCode(code).orElse(null);
    }

    public void deleteByCode(String code) {
        actionCodeRepository.deleteByCode(code);
    }

    public int deleteExpiresActionCodes() {
        var actionCodes = actionCodeRepository.findAllExpires(Instant.now());
        actionCodeRepository.deleteAll(actionCodes);
        return actionCodes.size();
    }
}
