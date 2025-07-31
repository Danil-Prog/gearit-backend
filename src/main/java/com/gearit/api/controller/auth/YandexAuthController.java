package com.gearit.api.controller.auth;

import com.gearit.api.config.properties.YandexProperties;
import com.gearit.api.constants.http.CookieObjects.RefreshCookie;
import com.gearit.api.dto.response.LoginResponse;
import com.gearit.api.dto.response.TokenResponse;
import com.gearit.api.service.auth.YandexOAuthService;
import com.gearit.api.utils.http.HttpCookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/yandex")
public class YandexAuthController {

    private final YandexOAuthService yandexOAuthService;
    private final YandexProperties yandexProperties;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public YandexAuthController(
            YandexOAuthService yandexOAuthService,
            YandexProperties yandexProperties
    ) {
        this.yandexOAuthService = yandexOAuthService;
        this.yandexProperties = yandexProperties;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        final String url = yandexProperties.getAuthUri()
                + "?response_type=code"
                + "&client_id=" + yandexProperties.getClientId()
                + "&redirect_uri=" + yandexProperties.getRedirectUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<LoginResponse> callback(@RequestParam String code, HttpServletResponse servletResponse) {
        try {
            TokenResponse tokenResponse = yandexOAuthService.callbackAuthentication(code);
            HttpCookieUtils.setHttpCookie(servletResponse, new RefreshCookie(tokenResponse.refreshToken()));

            return ResponseEntity.ok(new LoginResponse(tokenResponse.accessToken()));
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
