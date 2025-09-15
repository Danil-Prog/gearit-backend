package com.gearit.api.controller.password;

import com.gearit.common.http.request.ForgotRequest;
import com.gearit.common.http.request.ResetPasswordRequest;
import com.gearit.common.http.request.VerifyCodeRequest;
import com.gearit.api.service.password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@RequestBody ForgotRequest forgotRequest) {
        passwordService.forgot(forgotRequest.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verify(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        passwordService.verifyCode(verifyCodeRequest.code());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordService.resetPassword(resetPasswordRequest.code(), resetPasswordRequest.newPassword());
        return ResponseEntity.ok().build();
    }
}
