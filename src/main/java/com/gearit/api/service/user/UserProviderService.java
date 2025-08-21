package com.gearit.api.service.user;

import com.gearit.api.dto.view.UserProviderView;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.repository.UserProviderRepository;
import com.gearit.api.service.profile.AccountInfoService;
import com.gearit.api.utils.http.PageableRequest;
import com.gearit.api.utils.validator.UserProviderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<UserProviderView> getUserProviders(PageableRequest<UserProvider> request) {
        Specification<UserProvider> spec = request.toSpecification();
        return userProviderRepository.findAll(spec, request.toPageable()).map(UserProviderView::from);
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

    @Transactional
    public UserProvider createUserProviderWithEmptyAccountInfo(UserProvider userProvider) {
        AccountInfo accountInfo = accountInfoService.createEmptyAccount();
        userProvider.setAccountInfo(accountInfo);

        // Ожидается что при `TypeProvider.INTERNAL` пароль не пустой
        if (userProvider.getProvider() == TypeProvider.INTERNAL) {
            userProvider.setPassword(bCryptPasswordEncoder.encode(userProvider.getPassword()));
        }

        return createUserProvider(userProvider);
    }

    public void createUserProviderWithAccountInfo(UserProvider userProvider, AccountInfo accountInfo) {
        AccountInfo savedAccountInfo = accountInfoService.createAccount(accountInfo);
        userProvider.setAccountInfo(savedAccountInfo);
        createUserProvider(userProvider);
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

    private UserProvider createUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);
        return userProviderRepository.save(userProvider);
    }
}

