package com.gearit.api.service.profile;

import com.gearit.api.dto.request.UpdateAccountInfoRequest;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.repository.AccountInfoRepository;
import com.gearit.api.utils.AccountInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    private final Logger logger = LoggerFactory.getLogger(AccountInfoService.class);

    @Autowired
    public AccountInfoService(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    public void updateAccountInfo(AccountInfo accountInfo, UpdateAccountInfoRequest request) {
        accountInfo.setFirstName(request.firstName());
        accountInfo.setMiddleName(request.middleName());
        accountInfo.setLastName(request.lastName());
        accountInfo.setGender(request.gender());
        accountInfo.setPhoneNumber(request.phoneNumber());
        accountInfo.setBirthDate(request.birthDate());

        AccountInfoValidator.validateAccountInfo(accountInfo);

        accountInfoRepository.save(accountInfo);
    }

    /**
     * Создает пустой аккаунт, необходим на этапе регистрации пользователя
     *
     * @return `AccountInfo` - пустой аккаунт
     */
    public AccountInfo createEmptyAccount() {
        return accountInfoRepository.save(new AccountInfo());
    }

    public AccountInfo createAccount(AccountInfo accountInfo) {
        return accountInfoRepository.save(accountInfo);
    }
}
