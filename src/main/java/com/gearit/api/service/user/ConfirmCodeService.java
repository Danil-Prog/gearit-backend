package com.gearit.api.service.user;

import com.gearit.api.entity.user.ConfirmCode;
import com.gearit.api.repository.ConfirmCodeRepository;
import java.util.UUID;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
public class ConfirmCodeService {

    private final ConfirmCodeRepository confirmCodeRepository;

    public ConfirmCodeService(ConfirmCodeRepository confirmCodeRepository) {
        this.confirmCodeRepository = confirmCodeRepository;
    }

    public ConfirmCode createNewConfirmCode(Long userProviderId) {
        var randomCode = UUID.randomUUID().toString();

        var confirmCode = new ConfirmCode();
        confirmCode.setUserProviderId(userProviderId);
        confirmCode.setCode(randomCode);

        return confirmCodeRepository.save(confirmCode);
    }

    public ConfirmCode getConfirmCodeByCode(String code) {
        return confirmCodeRepository.findByCode(code).orElse(null);
    }
}
