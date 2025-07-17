package com.gearit.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cors.properties")
public class CorsProperties {

    private String corsAllowedOriginsUri;

    public String getCorsAllowedOriginsUri() {
        return corsAllowedOriginsUri;
    }

    public void setCorsAllowedOriginsUri(String corsAllowedOriginsUri) {
        this.corsAllowedOriginsUri = corsAllowedOriginsUri;
    }
}
