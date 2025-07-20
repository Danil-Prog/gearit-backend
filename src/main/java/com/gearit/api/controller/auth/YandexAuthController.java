package com.gearit.api.controller.auth;

import com.gearit.api.config.properties.*;
import com.gearit.api.dto.response.*;
import com.gearit.api.service.auth.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TokenResponse> callback(@RequestParam String code) {
        try {
            TokenResponse response = yandexOAuthService.callbackAuthentication(code);
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
