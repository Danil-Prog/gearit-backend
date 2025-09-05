package com.gearit.api.config;

import com.gearit.api.config.properties.EmailProperties;
import com.resend.Resend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    private final EmailProperties emailProperties;

    @Autowired
    public ApplicationConfig(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public Resend resend() {
        return new Resend(emailProperties.getResendToken());
    }
}
