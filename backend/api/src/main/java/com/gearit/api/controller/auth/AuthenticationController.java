package com.gearit.api.controller.auth;

import com.gearit.common.http.cookie.CookieObjects.RefreshCookie;
import com.gearit.common.http.request.LoginRequest;
import com.gearit.common.http.request.RegisterRequest;
import com.gearit.common.http.response.LoginResponse;
import com.gearit.common.http.response.RefreshResponse;
import com.gearit.common.http.response.RegisterResponse;
import com.gearit.api.service.auth.AuthService;
import com.gearit.common.http.cookie.HttpCookieUtils;
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
    public ResponseEntity<Void> verify(@RequestParam String code) {
        authService.verifyUserProvider(code);
        return ResponseEntity.ok().build();
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
        try {
            var tokenResponse = authService.refreshToken(refreshToken);
            HttpCookieUtils.setHttpCookie(servletResponse, new RefreshCookie(tokenResponse.refreshToken()));
            return ResponseEntity.ok(new RefreshResponse(tokenResponse.accessToken()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }
}
