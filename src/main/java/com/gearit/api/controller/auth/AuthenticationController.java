package com.gearit.api.controller.auth;

import com.gearit.api.controller.request.LoginRequest;
import com.gearit.api.controller.request.RegisterRequest;
import com.gearit.api.controller.request.TokenRequest;
import com.gearit.api.controller.response.ConfirmResponse;
import com.gearit.api.controller.response.RegisterResponse;
import com.gearit.api.controller.response.TokenResponse;
import com.gearit.api.service.auth.AuthService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/headers")
    public ResponseEntity<?> headers(@RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(headers);
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
}
