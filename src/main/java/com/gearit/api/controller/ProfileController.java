package com.gearit.api.controller;

import com.gearit.api.controller.response.ProfileResponse;
import com.gearit.api.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final NotificationService notificationService;

    @Autowired
    public ProfileController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping
    public ResponseEntity<ProfileResponse> profile(Authentication auth) {
        System.out.println(auth.toString());
        System.out.println(auth.getPrincipal().toString());

        notificationService.sendNotificationToEmail();
        return ResponseEntity.ok(new ProfileResponse(auth.getPrincipal().toString()));
    }
}