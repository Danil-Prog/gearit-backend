package com.gearit.api.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProviderService userProviderService;

    public JwtOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider,
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
    ) throws IOException {
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

        var tokens = Map.of(
                "accessToken", jwtTokenProvider.generateAccessToken(email),
                "refreshToken", jwtTokenProvider.generateRefreshToken(email)
        );

        System.out.println(tokens);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }
}
