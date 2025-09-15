package com.gearit.common.http.request;

public record ResetPasswordRequest(String code, String newPassword) {
}
