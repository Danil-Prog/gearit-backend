package com.gearit.api.service;

import com.gearit.api.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс для получения служебной информации о приложении.
 */
@Service
public class ApplicationService {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public ApplicationService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getApplicationVersion() {
        return applicationProperties.getVersion();
    }
}
