package com.gearit.api.service.profile;

import com.gearit.api.entity.account.AccountGender;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.repository.AccountInfoRepository;
import com.gearit.api.utils.validator.AccountInfoValidator;
import com.gearit.common.http.request.UpdateAccountInfoRequest;
import com.gearit.common.utils.EnumConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    @Autowired
    public AccountInfoService(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    public void updateAccountInfo(AccountInfo accountInfo, UpdateAccountInfoRequest request) {
        AccountGender gender = EnumConverter.toEnum(AccountGender.class, request.gender());

        accountInfo.setFirstName(request.firstName());
        accountInfo.setMiddleName(request.middleName());
        accountInfo.setLastName(request.lastName());
        accountInfo.setGender(gender);
        accountInfo.setPhoneNumber(request.phoneNumber());
        accountInfo.setBirthDate(request.birthDate());

        AccountInfoValidator.validate(accountInfo);

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
