package com.gearit.api.service.auth;

import com.gearit.api.dto.response.TokenResponse;
import com.gearit.api.entity.actioncode.ActionCode;
import com.gearit.api.entity.actioncode.ActionType;
import com.gearit.api.entity.notification.NotificationTemplate;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.service.actioncode.ActionCodeService;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.notification.NotificationService;
import com.gearit.api.service.user.UserProviderService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProviderService userProviderService;
    private final AuthenticationManager authenticationManager;
    private final ActionCodeService actionCodeService;
    private final NotificationService notificationService;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserProviderService userProviderService,
            AuthenticationManager authenticationManager,
            ActionCodeService actionCodeService,
            NotificationService notificationService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
        this.authenticationManager = authenticationManager;
        this.actionCodeService = actionCodeService;
        this.notificationService = notificationService;
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
        userProvider.setProvider(TypeProvider.INTERNAL);

        Long userProviderId = userProviderService.createUserProviderWithEmptyAccountInfo(userProvider).getId();
        ActionCode actionCode = actionCodeService.createCode(userProviderId, ActionType.CONFIRM_USER);

        notificationService.createNotification(
                NotificationTemplate.USER_CONFIRMED,
                userProvider,
                Map.of("code", actionCode.getCode())
        );

        logger.info("New user with email: {} created", email);
    }

    @Transactional
    public void verifyUserProvider(String code) {
        ActionCode actionCode = actionCodeService.findByCode(code);

        if (actionCode == null) {
            throw new WebClientException("User confirmed failed", "Verification code sent is invalid.");
        }

        UserProvider userProvider = userProviderService.getUserProviderById(actionCode.getUserProviderId());
        userProvider.setIsConfirmed(true);

        // Подтверждаем аккаунт пользователя и удаляем код подтверждения из БД.
        userProviderService.updateUserProvider(userProvider);
        actionCodeService.deleteByCode(actionCode.getCode());

        logger.info("Verify user with email: [{}], confirm code {}", userProvider.getEmail(), actionCode.getCode());
    }

    public TokenResponse login(String email, String password) {
        var errorMessage = "User authentication failed";
        var user = userProviderService.getUserProviderByEmailOrThrow(email);

        if (user.getIsConfirmed() == false) {
            throw new WebClientException(errorMessage, "User is not confirmed");
        }

        try {
            var authentication = new UsernamePasswordAuthenticationToken(user, password);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException exception) {
            throw new WebClientException(errorMessage, "Invalid email or password");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(email);
        var refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
