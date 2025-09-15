package com.gearit.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class HttpUtils {

    private static final String BEARER_PREFIX = "Bearer ";

    public static String getBearerAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = null;

        if (header != null && !header.isBlank() && header.startsWith(BEARER_PREFIX)) {
            token = header.substring(BEARER_PREFIX.length());
        }

        return token;
    }
}
