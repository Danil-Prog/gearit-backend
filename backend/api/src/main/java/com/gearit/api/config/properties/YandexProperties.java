package com.gearit.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("yandex.properties")
public class YandexProperties {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String authUri;

    private String tokenUri;

    private String userInfoUri;
}
