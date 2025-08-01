package com.gearit.api.controller.auth;

import com.gearit.api.constants.http.CookieObjects.RefreshCookie;
import com.gearit.api.dto.request.LoginRequest;
import com.gearit.api.dto.request.RegisterRequest;
import com.gearit.api.dto.response.ConfirmResponse;
import com.gearit.api.dto.response.LoginResponse;
import com.gearit.api.dto.response.RefreshResponse;
import com.gearit.api.dto.response.RegisterResponse;
import com.gearit.api.service.auth.AuthService;
import com.gearit.api.utils.http.HttpCookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse servletResponse
    ) {
        var tokenResponse = authService.login(loginRequest.email(), loginRequest.password());
        HttpCookieUtils.setHttpCookie(servletResponse, new RefreshCookie(tokenResponse.refreshToken()));

        return ResponseEntity.ok(new LoginResponse(tokenResponse.accessToken()));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(name = RefreshCookie.NAME) String refreshToken,
            HttpServletResponse servletResponse
    ) {
        var tokenResponse = authService.refreshToken(refreshToken);
        HttpCookieUtils.setHttpCookie(servletResponse, new RefreshCookie(tokenResponse.refreshToken()));

        return ResponseEntity.ok(new RefreshResponse(tokenResponse.accessToken()));
    }
}
