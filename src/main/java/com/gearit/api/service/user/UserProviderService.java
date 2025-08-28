package com.gearit.api.service.user;

import com.gearit.api.dto.request.UpdateAccessPolicyUserProviderRequest;
import com.gearit.api.dto.view.UserProviderView;
import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.repository.UserProviderRepository;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import com.gearit.api.service.profile.AccountInfoService;
import com.gearit.api.utils.http.PageableRequest;
import com.gearit.api.utils.validator.UserProviderValidator;
import java.util.Objects;
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
    private final AccessPolicyService accessPolicyService;

    @Autowired
    public UserProviderService(
            UserProviderRepository userProviderRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AccountInfoService accountInfoService,
            AccessPolicyService accessPolicyService
    ) {
        this.userProviderRepository = userProviderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountInfoService = accountInfoService;
        this.accessPolicyService = accessPolicyService;
    }

    public Page<UserProviderView> getUserProviders(PageableRequest<UserProvider> request) {
        Specification<UserProvider> spec = request.toSpecification();
        return userProviderRepository.findAll(spec, request.toPageable()).map(UserProviderView::from);
    }

    public UserProvider getUserProviderByEmailOrNull(String email) {
        return userProviderRepository.findByEmail(email).orElse(null);
    }

    public Boolean isUserProviderByEmailExist(String email) {
        return userProviderRepository.findByEmail(email).isPresent();
    }

    public UserProvider getUserProviderByEmailOrThrow(String email) {
        return userProviderRepository.findByEmail(email).orElseThrow(() ->
                new WebClientException(
                        "Cannot get user by email address.",
                        "User with this email address was not found."
                )
        );
    }

    public UserProvider getUserProviderByIdOrThrow(Long id) {
        return userProviderRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Cannot get user by email address.",
                        "User with this email address was not found."
                )
        );
    }

    public UserProvider getUserProviderById(Long id) {
        return userProviderRepository.findById(id).orElse(null);
    }

    @Transactional
    public UserProvider createUserProviderWithEmptyAccountInfo(UserProvider userProvider) {
        AccountInfo accountInfo = accountInfoService.createEmptyAccount();
        userProvider.setAccountInfo(accountInfo);

        return createUserProvider(userProvider);
    }

    public void createUserProviderWithAccountInfo(UserProvider userProvider, AccountInfo accountInfo) {
        AccountInfo savedAccountInfo = accountInfoService.createAccount(accountInfo);
        userProvider.setAccountInfo(savedAccountInfo);

        createUserProvider(userProvider);
    }

    public void updateUserProviderPassword(Long id, String password) {
        UserProvider userProvider = getUserProviderByIdOrThrow(id);
        userProvider.setPassword(password);
        updateUserProvider(userProvider);
    }

    public void updateUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);

        String encodedPassword = bCryptPasswordEncoder.encode(userProvider.getPassword());
        userProvider.setPassword(encodedPassword);

        userProviderRepository.save(userProvider);
    }

    /**
     * Обновляет политику доступа для пользователя.
     *
     * @param request - Идентификаторы пользователя и политики доступа.
     */
    public void updateAccessPolicyUserProvider(UpdateAccessPolicyUserProviderRequest request) {
        final String ERROR_MESSAGE = "Failed to assign user access policy";
        UserProvider userProvider = getUserProviderByIdOrThrow(request.userProviderId());

        if (Objects.equals(userProvider.getAccessPolicy().getId(), request.accessPolicyId())) {
            throw new WebClientException(
                    ERROR_MESSAGE,
                    "User is already assigned such an access policy"
            );
        }

        AccessPolicy accessPolicy = accessPolicyService.getAccessPolicyById(request.accessPolicyId());

        if (accessPolicy == null) {
            throw new WebClientException(
                    ERROR_MESSAGE,
                    String.format("Access policy with this id: [%s] does not exist", request.accessPolicyId())
            );
        }

        userProvider.setAccessPolicy(accessPolicy);

        updateUserProvider(userProvider);
    }

    private UserProvider createUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);

        // Шифруем пароль только после всех валидаций и только для `внутренней` регистрации
        if (userProvider.getProvider() == TypeProvider.INTERNAL) {
            String encodedPassword = bCryptPasswordEncoder.encode(userProvider.getPassword());
            userProvider.setPassword(encodedPassword);
        }

        // Политика доступа по умолчанию для новых пользователей.
        var accessPolicy = accessPolicyService.getAccessPolicyByName("CLIENT");

        userProvider.setAccessPolicy(accessPolicy);
        return userProviderRepository.save(userProvider);
    }
}

