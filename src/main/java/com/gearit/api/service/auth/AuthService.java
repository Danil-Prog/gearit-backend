package com.gearit.api.service.auth;

import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.entity.user.ConfirmCode;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.notification.NotificationService;
import com.gearit.api.service.user.ConfirmCodeService;
import com.gearit.api.service.user.UserProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProviderService userProviderService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmCodeService confirmCodeService;
    private final NotificationService notificationService;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserProviderService userProviderService,
            AuthenticationManager authenticationManager,
            ConfirmCodeService confirmCodeService,
            NotificationService notificationService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
        this.authenticationManager = authenticationManager;
        this.confirmCodeService = confirmCodeService;
        this.notificationService = notificationService;
    }

    public void register(String email, String password) {
        var user = userProviderService.getUserProviderByEmailOrNull(email);

        if (user != null) {
            throw new BadRequestException("User with such data already exists");
        }
        UserProvider userProvider = new UserProvider();

        userProvider.setEmail(email);
        userProvider.setPassword(password);
        userProvider.setProvider(TypeProvider.INTERNAL.name());
        userProvider.setConfirmed(false);

        Long userProviderId = userProviderService.createUserProvider(userProvider).getId();
        ConfirmCode code = confirmCodeService.createNewConfirmCode(userProviderId);

        notificationService.sendConfirmEmail(email, code.getCode());

        logger.info("New user created, send confirm code {}, to user with email: {}", code.getCode(), email);
    }

    public void verifyUserProvider(String code) {
        ConfirmCode confirmCode = confirmCodeService.getConfirmCodeByCode(code);

        if (confirmCode == null) {
            throw new BadRequestException("Verification code sent is invalid.");
        }

        UserProvider userProvider = userProviderService.getUserProviderById(confirmCode.getUserProviderId());
        userProvider.setConfirmed(true);

        userProviderService.updateUserProvider(userProvider);

        logger.info("Verify user with email: [{}], confirm code {}", userProvider.getEmail(), confirmCode.getCode());
    }

    public TokenResponse login(String email, String password) {
        var user = userProviderService.getUserProviderByEmailOrNull(email);

        if (user == null) {
            throw new BadRequestException("Invalid email or password");
        }

        if (user.getConfirmed() == false) {
            throw new BadRequestException("User is not confirmed");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException exception) {
            throw new BadRequestException("Invalid email or password");
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
