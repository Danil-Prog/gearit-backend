package com.gearit.api.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProviderService userProviderService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JwtOAuth2SuccessHandler(
            JwtTokenProvider jwtTokenProvider,
            UserProviderService userProviderService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        try {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String email = oauthUser.getAttributes().get("default_email").toString();
            String username = oauthUser.getAttributes().get("login").toString();

            // Создаём или находим пользователя
            UserProvider userProvider = userProviderService.findUserProviderByEmailOrNull(email);

            if (userProvider == null) {
                UserProvider newUserProvider = new UserProvider();

                // пустой пароль задается исключительно при авторизации через oath2
                newUserProvider.setPassword("");
                newUserProvider.setUsername(username);
                newUserProvider.setEmail(email);
                newUserProvider.setProvider(TypeProvider.OAUTH.name());

                userProviderService.createUserProvider(newUserProvider);
            }

            TokenResponse tokens = new TokenResponse(
                    jwtTokenProvider.generateAccessToken(email),
                    jwtTokenProvider.generateRefreshToken(email)
            );

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            new ObjectMapper().writeValue(response.getWriter(), tokens);
        } catch (Exception e) {
            logger.error("Error while generating token for oauth user, error: {}", e.getMessage());
        }
    }
}
