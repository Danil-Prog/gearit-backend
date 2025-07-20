package com.gearit.api.dto.request;

public record ResetPasswordRequest(String code, String password) {
}
