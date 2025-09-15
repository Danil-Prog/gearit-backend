package com.gearit.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.http.HttpHeaders;

public class HttpUtils {

    public static Optional<String> getBearerAccessTokenFromHeader(HttpServletRequest request) {
        return Optional.of(request.getHeader(HttpHeaders.AUTHORIZATION));
    }
}
