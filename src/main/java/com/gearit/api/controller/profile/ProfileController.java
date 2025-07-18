package com.gearit.api.controller.profile;

import com.gearit.api.controller.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @GetMapping
    public ResponseEntity<ProfileResponse> profile(Authentication auth) {
        return ResponseEntity.ok(new ProfileResponse(auth.getPrincipal().toString()));
    }
}