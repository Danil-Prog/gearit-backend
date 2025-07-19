package com.gearit.api.controller.auth;

import com.gearit.api.controller.request.*;
import com.gearit.api.controller.response.*;
import com.gearit.api.service.auth.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest.email(), registerRequest.password());
        return ResponseEntity.ok(new RegisterResponse());
    }

    @GetMapping("/verify")
    public ResponseEntity<ConfirmResponse> verify(@RequestParam String code) {
        authService.verifyUserProvider(code);
        return ResponseEntity.ok(new ConfirmResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        var response = authService.login(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody TokenRequest tokenRequest) {
        var response = authService.refreshToken(tokenRequest.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/recovery/notification")
    public ResponseEntity<PasswordRecoveryResponse> sendRecoveryPasswordNotification(
            @RequestBody PasswordRecoveryRequest passwordRecoveryRequest
    ) {
        authService.createAndSendRecoveryPasswordNotification(passwordRecoveryRequest.email());
        return ResponseEntity.ok(new PasswordRecoveryResponse());
    }
}
