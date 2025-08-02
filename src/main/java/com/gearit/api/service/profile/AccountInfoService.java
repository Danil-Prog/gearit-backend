package com.gearit.api.service.profile;

import com.gearit.api.repository.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    @Autowired
    public AccountInfoService(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }
}
