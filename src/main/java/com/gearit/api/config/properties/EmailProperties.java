package com.gearit.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("email.properties")
public class EmailProperties {

    private String resendToken;

    private String from;

    private String urlVerify;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getResendToken() {
        return resendToken;
    }

    public void setResendToken(String resendToken) {
        this.resendToken = resendToken;
    }

    public String getUrlVerify() {
        return urlVerify;
    }

    public void setUrlVerify(String urlVerify) {
        this.urlVerify = urlVerify;
    }
}
