package com.gearit.api.controller.request;

public record ResetPasswordRequest(String code, String password) {
}
