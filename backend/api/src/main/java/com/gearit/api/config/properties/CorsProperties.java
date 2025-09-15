package com.gearit.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("cors.properties")
public class CorsProperties {

    private String corsAllowedOriginsUri;
}
