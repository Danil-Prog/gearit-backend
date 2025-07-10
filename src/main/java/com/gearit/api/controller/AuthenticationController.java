package com.gearit.api.controller;

import com.gearit.api.controller.request.LoginRequest;
import com.gearit.api.controller.request.RegisterRequest;
import com.gearit.api.controller.request.TokenRequest;
import com.gearit.api.controller.response.RegisterResponse;
import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.service.auth.AuthService;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserProviderService userProviderService;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(
            UserProviderService userProviderService,
            AuthService authService
    ) {
        this.userProviderService = userProviderService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        var user = userProviderService.findUserProviderByLoginOrEmailOrNull(
                registerRequest.username(),
                registerRequest.email()
        );

        if (user != null) {
            throw new BadRequestException("User with such data already exists");
        }
        UserProvider userProvider = new UserProvider();

        userProvider.setEmail(registerRequest.email());
        userProvider.setPassword(registerRequest.password());
        userProvider.setUsername(registerRequest.username());
        userProvider.setProvider(TypeProvider.INTERNAL.name());

        userProviderService.createUserProvider(userProvider);

        return ResponseEntity.ok(new RegisterResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        var response = authService.login(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequest tokenRequest) {
        var response = authService.refreshToken(tokenRequest.refreshToken());
        return ResponseEntity.ok(response);
    }
}
