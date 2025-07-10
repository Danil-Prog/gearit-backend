package com.gearit.api.service.auth;

import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
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

    @Autowired
    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserProviderService userProviderService,
            AuthenticationManager authenticationManager
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse login(String username, String password) {
        var user = userProviderService.findUserProviderByEmailOrNull(username);

        if (user == null) {
            throw new BadRequestException("Invalid username or password");
        }

        if (user.getConfirmed() == false) {
            throw new BadRequestException("User is not confirmed");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException exception) {
            throw new BadRequestException("Invalid username or password");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(username);
        var refreshToken = jwtTokenProvider.generateRefreshToken(username);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(username);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

            return new TokenResponse(newAccessToken, newRefreshToken);
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
