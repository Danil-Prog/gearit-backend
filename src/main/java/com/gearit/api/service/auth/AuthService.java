package com.gearit.api.service.auth;

import com.gearit.api.dto.response.*;
import com.gearit.api.entity.notification.*;
import com.gearit.api.entity.user.*;
import com.gearit.api.exception.*;
import com.gearit.api.service.confirmcode.*;
import com.gearit.api.service.jwt.*;
import com.gearit.api.service.notification.*;
import com.gearit.api.service.passwordrecovery.*;
import com.gearit.api.service.user.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProviderService userProviderService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmCodeService confirmCodeService;
    private final NotificationService notificationService;
    private final PasswordRecoveryService passwordRecoveryService;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserProviderService userProviderService,
            AuthenticationManager authenticationManager,
            ConfirmCodeService confirmCodeService,
            NotificationService notificationService,
            PasswordRecoveryService passwordRecoveryService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
        this.authenticationManager = authenticationManager;
        this.confirmCodeService = confirmCodeService;
        this.notificationService = notificationService;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @Transactional
    public void register(String email, String password) {
        var user = userProviderService.getUserProviderByEmailOrNull(email);

        if (user != null) {
            throw new BadRequestException("User with such data already exists");
        }
        UserProvider userProvider = new UserProvider();

        userProvider.setEmail(email);
        userProvider.setPassword(password);
        userProvider.setProvider(TypeProvider.INTERNAL.name());

        Long userProviderId = userProviderService.createUserProvider(userProvider).getId();
        ConfirmCode confirmCode = confirmCodeService.createNewConfirmCode(userProviderId);

        notificationService.createNotification(
                NotificationTemplate.USER_CONFIRMED,
                userProvider,
                Map.of("code", confirmCode.getCode())
        );

        logger.info("New user with email: {} created", email);
    }

    @Transactional
    public void verifyUserProvider(String code) {
        ConfirmCode confirmCode = confirmCodeService.getConfirmCodeByCode(code);

        if (confirmCode == null) {
            throw new WebClientException("User confirmed failed", "Verification code sent is invalid.");
        }

        UserProvider userProvider = userProviderService.getUserProviderById(confirmCode.getUserProviderId());
        userProvider.setConfirmed(true);

        // Подтверждаем аккаунт пользователя и удаляем код подтверждения из БД.
        userProviderService.updateUserProvider(userProvider);
        confirmCodeService.deleteConfirmCodeByCode(confirmCode.getCode());

        logger.info("Verify user with email: [{}], confirm code {}", userProvider.getEmail(), confirmCode.getCode());
    }

    public TokenResponse login(String email, String password) {
        var errorMessage = "User authentication failed";
        var user = userProviderService.getUserProviderByEmailOrThrow(email);

        if (user.isConfirmed() == false) {
            throw new WebClientException(errorMessage, "User is not confirmed");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException exception) {
            throw new WebClientException(errorMessage, "Invalid email or password");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(email);
        var refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);

            String newAccessToken = jwtTokenProvider.generateAccessToken(email);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

            return new TokenResponse(newAccessToken, newRefreshToken);
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

}
