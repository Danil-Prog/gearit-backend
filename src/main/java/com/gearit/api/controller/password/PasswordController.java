package com.gearit.api.controller.password;

import com.gearit.api.dto.request.*;
import com.gearit.api.service.password.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok().build();
    }
}
