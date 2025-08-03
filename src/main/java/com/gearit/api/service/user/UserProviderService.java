package com.gearit.api.service.user;

import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.repository.UserProviderRepository;
import com.gearit.api.service.profile.AccountInfoService;
import com.gearit.api.utils.UserProviderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProviderService {

    private final UserProviderRepository userProviderRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountInfoService accountInfoService;

    @Autowired
    public UserProviderService(
            UserProviderRepository userProviderRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AccountInfoService accountInfoService
    ) {
        this.userProviderRepository = userProviderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountInfoService = accountInfoService;
    }

    public UserProvider getUserProviderByEmailOrNull(String email) {
        return userProviderRepository.findByEmail(email).orElse(null);
    }

    public UserProvider getUserProviderByEmailOrThrow(String email) {
        return userProviderRepository.findByEmail(email).orElseThrow(() ->
                new BadRequestException("User with this email address was not found.")
        );
    }

    public UserProvider getUserProviderById(Long id) {
        return userProviderRepository.findById(id).orElse(null);
    }

    public UserProvider createUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);

        // Ожидается что при `TypeProvider.INTERNAL` пароль не пустой
        if (userProvider.getProvider().equals(TypeProvider.INTERNAL)) {
            userProvider.setPassword(bCryptPasswordEncoder.encode(userProvider.getPassword()));
        }

        AccountInfo accountInfo = accountInfoService.createEmptyAccount();
        userProvider.setAccountInfo(accountInfo);

        return userProviderRepository.save(userProvider);
    }

    public void updateUserProviderPassword(Long id, String password) {
        UserProvider userProvider = getUserProviderById(id);
        if (userProvider == null) {
            throw new WebClientException("Couldn't update password", "User with not found");
        }

        userProvider.setPassword(bCryptPasswordEncoder.encode(password));
        updateUserProvider(userProvider);
    }

    public void updateUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);
        userProviderRepository.save(userProvider);
    }
}

