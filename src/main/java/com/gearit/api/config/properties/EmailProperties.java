package com.gearit.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("email.properties")
public class EmailProperties {

    private String resendToken;

    public String getResendToken() {
        return resendToken;
    }

    public void setResendToken(String resendToken) {
        this.resendToken = resendToken;
    }
}
