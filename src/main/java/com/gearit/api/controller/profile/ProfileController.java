package com.gearit.api.controller.profile;

import com.gearit.api.dto.response.ProfileResponse;
import com.gearit.api.entity.user.*;
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
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        return ResponseEntity.ok(new ProfileResponse(userProvider.getUsername()));
    }
}