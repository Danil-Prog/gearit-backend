package com.gearit.api.service.auth;

import com.gearit.api.config.properties.YandexProperties;
import com.gearit.api.dto.response.TokenResponse;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YandexOAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final YandexProperties yandexProperties;
    private final UserProviderService userProviderService;
    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public YandexOAuthService(
            JwtTokenProvider jwtTokenProvider,
            YandexProperties yandexProperties,
            UserProviderService userProviderService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.yandexProperties = yandexProperties;
        this.userProviderService = userProviderService;
        this.restTemplate = new RestTemplate();
    }

    public TokenResponse callbackAuthentication(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=authorization_code"
                + "&code=" + code
                + "&client_id=" + yandexProperties.getClientId()
                + "&client_secret=" + yandexProperties.getClientSecret();

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                yandexProperties.getTokenUri(),
                request,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            logger.error("Request for user data could not be completed, status code: {}", response.getStatusCode());
            throw new BadRequestException("Invalid authorization code.");
        }

        String accessToken = response.getBody().get("access_token").toString();

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.set("Authorization", "OAuth " + accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                yandexProperties.getUserInfoUri(),
                HttpMethod.GET,
                userInfoRequest,
                Map.class
        );

        if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
            logger.error("Couldn't get user data, status code: {}", userInfoResponse.getStatusCode());
            throw new BadRequestException("Couldn't get user data");
        }

        String yandexId = userInfoResponse.getBody().get("id").toString();
        String email = userInfoResponse.getBody().get("default_email").toString();

        saveYandexUser(email);

        logger.info("Client with ID: {}, successfully authorized!", yandexId);

        return new TokenResponse(
                jwtTokenProvider.generateAccessToken(email),
                jwtTokenProvider.generateRefreshToken(email)
        );
    }

    private void saveYandexUser(String email) {
        UserProvider userProvider = userProviderService.getUserProviderByEmailOrNull(email);

        if (userProvider == null) {
            UserProvider newUserProvider = new UserProvider();

            // пустой пароль задается исключительно при авторизации через oath2
            newUserProvider.setPassword("");
            newUserProvider.setEmail(email);
            newUserProvider.setProvider(TypeProvider.OAUTH.name());
            newUserProvider.setConfirmed(true);

            userProviderService.createUserProvider(newUserProvider);

            logger.info("Created new user provider from Yandex oauth2");
        } else {
            logger.info("Yandex user provider already exists");
        }
    }
}
