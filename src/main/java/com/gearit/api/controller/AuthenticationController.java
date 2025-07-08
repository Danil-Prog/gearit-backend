package com.gearit.api.controller;

import com.gearit.api.controller.request.LoginRequest;
import com.gearit.api.controller.request.RegisterRequest;
import com.gearit.api.controller.request.TokenRequest;
import com.gearit.api.controller.response.LoginResponse;
import com.gearit.api.controller.response.RegisterResponse;
import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserProviderService userProviderService;

    @Autowired
    public AuthenticationController(
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserProviderService userProviderService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userProviderService = userProviderService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        var user = userProviderService.findUserProviderByLoginOrEmailOrNull(
                registerRequest.username(),
                registerRequest.email()
        );

        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User with email: '" + registerRequest.email() + "' already exists");
        }
        UserProvider userProvider = new UserProvider();

        userProvider.setEmail(registerRequest.email());
        userProvider.setPassword(registerRequest.password());
        userProvider.setUsername(registerRequest.username());
        userProvider.setProvider(TypeProvider.INTERNAL.name());

        userProviderService.createUserProvider(userProvider);

        return ResponseEntity.ok(new RegisterResponse());
    }

    @GetMapping("/code")
    public ResponseEntity<?> test(
            @RequestParam(value = "state") String state,
            @RequestParam(value = "code") String code
    ) {
        System.out.println("code: " + code);
        System.out.println("state: " + state);

        return ResponseEntity.ok("Your API in work!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        LoginResponse loginResponse = new LoginResponse(
                tokenProvider.generateAccessToken(loginRequest.username()),
                tokenProvider.generateRefreshToken(loginRequest.username())
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequest tokenRequest) {
        if (jwtTokenProvider.validateToken(tokenRequest.refreshToken())) {
            String username = jwtTokenProvider.getUsernameFromToken(tokenRequest.refreshToken());
            String newAccessToken = jwtTokenProvider.generateAccessToken(username);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

            return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
}
