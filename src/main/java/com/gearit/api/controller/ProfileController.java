package com.gearit.api.controller;

import com.gearit.api.controller.response.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @GetMapping
    public ResponseEntity<ProfileResponse> profile() throws Exception {
        return ResponseEntity.ok(new ProfileResponse());
    }
}
