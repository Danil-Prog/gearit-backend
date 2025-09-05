package com.gearit.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("email.properties")
public class EmailProperties {

    private String resendToken;

    private String from;
}
