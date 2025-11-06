package com.gearit.common.http.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
