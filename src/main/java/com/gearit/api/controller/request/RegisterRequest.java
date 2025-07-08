package com.gearit.api.controller.request;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
