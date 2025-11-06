package com.gearit.api.controller.application;

import com.gearit.api.service.ApplicationService;
import com.gearit.common.http.response.GetApplicationVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/version")
    public ResponseEntity<GetApplicationVersion> getApplicationVersion() {
        String version = applicationService.getApplicationVersion();
        var response = new GetApplicationVersion(version);
        return ResponseEntity.ok(response);
    }
}
