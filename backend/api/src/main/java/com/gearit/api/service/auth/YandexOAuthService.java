package com.gearit.api.service.auth;

import com.gearit.api.config.properties.YandexProperties;
import com.gearit.api.dto.yandex.YandexPassport;
import com.gearit.api.dto.yandex.YandexToken;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.user.UserProviderService;
import com.gearit.common.exception.WebClientException;
import com.gearit.common.http.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserProviderService userProviderService;
    private final RestTemplate restTemplate;
    private final YandexProperties yandexProperties;

    private final Logger logger = LoggerFactory.getLogger(YandexOAuthService.class);

    @Autowired
    public YandexOAuthService(
            JwtTokenProvider jwtTokenProvider,
            YandexProperties yandexProperties,
            UserProviderService userProviderService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProviderService = userProviderService;
        this.yandexProperties = yandexProperties;
        this.restTemplate = new RestTemplate();
    }

    public TokenResponse callbackAuthentication(String code) {
        String token = authorizeYandexRequest(code);
        YandexPassport yandexPassport = getYandexPassport(token);

        String email = yandexPassport.getDefaultEmail();
        AccountInfo accountInfo = yandexPassport.toAccountInfo();

        saveYandexUser(email, accountInfo);

        logger.info("Client with ID: {}, successfully authorized!", yandexPassport.getId());

        var accessToken = jwtTokenProvider.generateAccessToken(email);
        var refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveYandexUser(String email, AccountInfo accountInfo) {
        if (userProviderService.isUserProviderByEmailExist(email)) {
            return;
        }

        UserProvider newUserProvider = new UserProvider();

        // пустой пароль задается исключительно при авторизации через oauth2
        newUserProvider.setPassword("");
        newUserProvider.setEmail(email);
        newUserProvider.setProvider(TypeProvider.OAUTH);
        newUserProvider.setIsConfirmed(true);

        userProviderService.createUserProviderWithAccountInfo(newUserProvider, accountInfo);

        logger.info("New user registered using Yandex");
    }

    private YandexPassport getYandexPassport(String accessToken) {
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.set("Authorization", "OAuth " + accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<YandexPassport> userInfoResponse = restTemplate.exchange(
                yandexProperties.getUserInfoUri(),
                HttpMethod.GET,
                userInfoRequest,
                YandexPassport.class
        );

        if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
            logger.error("Couldn't get user data, status code: {}", userInfoResponse.getStatusCode());
            throw asWebClientException("Couldn't get user data");
        }

        return userInfoResponse.getBody();
    }

    private String authorizeYandexRequest(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String tokenUri = yandexProperties.getTokenUri();

        String requestBody = String.format(
                "grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s",
                code,
                yandexProperties.getClientId(),
                yandexProperties.getClientSecret()
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<YandexToken> response = restTemplate.postForEntity(tokenUri, request, YandexToken.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            logger.error("Request for user data could not be completed, status code: {}", response.getStatusCode());
            throw asWebClientException("Invalid authorization code.");
        }

        return response.getBody().getAccessToken();
    }

    private WebClientException asWebClientException(String message) {
        return new WebClientException("Authorization error via the Yandex service", message);
    }
}
