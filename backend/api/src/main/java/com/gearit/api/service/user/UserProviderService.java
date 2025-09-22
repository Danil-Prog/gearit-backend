package com.gearit.api.service.user;

import com.gearit.api.dto.view.UserProviderView;
import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.UserProviderRepository;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import com.gearit.api.service.profile.AccountInfoService;
import com.gearit.api.utils.validator.UserProviderValidator;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.filter.PageableRequest;
import com.gearit.common.http.request.BlockUserProvidersRequest;
import com.gearit.common.http.request.UpdateAccessPolicyUserProviderRequest;
import java.time.Instant;
import java.util.List;
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
        Specification<UserProvider> spec = request.getSpecification();
        return userProviderRepository.findAll(spec, request.toPageable()).map(UserProviderView::from);
    }

    /**
     * Проверяет, существует ли пользователь с такой почтой в системе.
     *
     * @param email - почтовый адрес пользователя.
     */
    public Boolean isUserProviderByEmailExist(String email) {
        return userProviderRepository.findByEmail(email).isPresent();
    }

    /**
     * Возвращает пользователя по переданному email, иначе возвращает исключение.
     *
     * @param email - почтовый адрес пользователя.
     * @return {@link UserProvider}
     */
    public UserProvider getUserProviderByEmailOrThrow(String email) {
        return userProviderRepository.findByEmail(email).orElseThrow(() ->
                new WebClientException(
                        "Cannot get user by email address.",
                        "User with this email address was not found."
                )
        );
    }

    /**
     * Возвращает пользователя по переданному id, иначе возвращает исключение.
     *
     * @param id - идентификатор пользователя.
     * @return {@link UserProvider}.
     */
    public UserProvider getUserProviderByIdOrThrow(Long id) {
        return userProviderRepository.findById(id).orElseThrow(() ->
                new WebClientException(
                        "Cannot get user by identifier.",
                        "User with this identifier was not found."
                )
        );
    }

    @Transactional
    public UserProvider createUserProviderWithEmptyAccountInfo(UserProvider userProvider) {
        AccountInfo accountInfo = accountInfoService.createEmptyAccount();
        userProvider.setAccountInfo(accountInfo);

        return createUserProvider(userProvider);
    }

    @Transactional
    public void createUserProviderWithAccountInfo(UserProvider userProvider, AccountInfo accountInfo) {
        AccountInfo savedAccountInfo = accountInfoService.createAccount(accountInfo);
        System.out.println("Сохранил аккаунт с инфой: " + accountInfo);
        userProvider.setAccountInfo(savedAccountInfo);

        createUserProvider(userProvider);
    }

    public void updateUserProviderPassword(Long id, String newPassword) {
        UserProvider userProvider = getUserProviderByIdOrThrow(id);

        userProvider.setPassword(newPassword);
        userProvider.setPasswordUpdatedAt(Instant.now());

        UserProviderValidator.validateUserProvider(userProvider);

        String encodedPassword = bCryptPasswordEncoder.encode(userProvider.getPassword());
        userProvider.setPassword(encodedPassword);

        userProviderRepository.save(userProvider);
    }

    public void updateUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);
        userProvider.setUpdatedAt(Instant.now());

        userProviderRepository.save(userProvider);
    }

    /**
     * Обновляет политику доступа для пользователя.
     *
     * @param request - идентификатор пользователя и политики доступа.
     */
    public void updateAccessPolicyUserProvider(UpdateAccessPolicyUserProviderRequest request) {
        final String errorMessage = "Failed to assign user access policy";
        UserProvider userProvider = getUserProviderByIdOrThrow(request.userProviderId());

        if (userProvider.getAccessPolicy().getId().equals(request.accessPolicyId())) {
            throw new WebClientException(
                    errorMessage,
                    "User is already assigned such an access policy"
            );
        }

        AccessPolicy accessPolicy = accessPolicyService.getAccessPolicyById(request.accessPolicyId());

        if (accessPolicy == null) {
            throw new WebClientException(
                    errorMessage,
                    String.format("Access policy with this id: [%s] does not exist", request.accessPolicyId())
            );
        }

        userProvider.setAccessPolicy(accessPolicy);

        updateUserProvider(userProvider);
    }

    /**
     * Блокирует пользователей по переданным ID.
     *
     * @param request - список идентификаторов пользователей.
     */
    public void blockUserProviders(BlockUserProvidersRequest request) {
        final List<UserProvider> userProviders = userProviderRepository.findAllById(request.ids());

        List<Long> userProviderIds = userProviders.stream().map(UserProvider::getId).toList();

        request.ids().removeAll(userProviderIds);

        if (!request.ids().isEmpty()) {
            throw new WebClientException(
                    "Failed to block user providers",
                    String.format("Users with ID: %s do not exist", request.ids())
            );
        }

        userProviders.forEach(userProvider -> {
            userProvider.setIsBlocked(true);
            userProvider.setUpdatedAt(Instant.now());
        });

        userProviderRepository.saveAll(userProviders);
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
        userProvider.setCreatedAt(Instant.now());

        return userProviderRepository.save(userProvider);
    }
}
