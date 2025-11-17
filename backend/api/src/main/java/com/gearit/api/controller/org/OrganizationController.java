package com.gearit.api.controller.org;

import com.gearit.api.entity.org.Organization;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.org.OrganizationService;
import com.gearit.common.http.pageable.PageableRequest;
import com.gearit.common.http.request.RegisterOrganizationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<?> getOrganizations(
            @RequestBody PageableRequest<Organization> request,
            Authentication authentication
    ) {
        UserProvider userProvider = (UserProvider) authentication.getPrincipal();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerOrganization(
            @RequestBody RegisterOrganizationRequest request,
            Authentication authentication
    ) {
        UserProvider userProvider = (UserProvider) authentication.getPrincipal();
        organizationService.registerOrganization(request, userProvider);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getOrganizationRequests() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    private ResponseEntity<Void> createRequestDeleteOrganization(@RequestParam("id") Long id) {
        organizationService.createRequestDeleteOrganization(id);
        return ResponseEntity.ok().build();
    }
}
