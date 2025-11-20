package com.gearit.api.controller.org;

import com.gearit.api.entity.comment.Comment;
import com.gearit.api.entity.org.Organization;
import com.gearit.api.entity.org.OrganizationRequest;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.org.OrganizationService;
import com.gearit.common.http.pageable.PageableRequest;
import com.gearit.common.http.pageable.PageableResponse;
import com.gearit.common.http.request.AddCommentToOrganizationRequest;
import com.gearit.common.http.request.NewOrganizationRequest;
import com.gearit.common.http.request.UpdateOrganizationRequestStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/delete")
    private ResponseEntity<Void> createRequestDeleteOrganization(@RequestParam("id") Long id) {
        organizationService.createRequestDeleteOrganization(id);
        return ResponseEntity.ok().build();
    }

    // ******** OrganizationRequest ********

    @PostMapping("/requests")
    public ResponseEntity<?> getOrganizationRequests(
            @RequestBody PageableRequest<OrganizationRequest> request,
            Authentication authentication
    ) {
        UserProvider userProvider = (UserProvider) authentication.getPrincipal();
        var organizationRequests = organizationService.getOrganizationRequests(request, userProvider);
        return PageableResponse.toResponseEntity(organizationRequests, request);
    }

    @PostMapping("/requests/new")
    public ResponseEntity<Void> newOrganizationRequest(
            @RequestBody NewOrganizationRequest request,
            Authentication authentication
    ) {
        UserProvider userProvider = (UserProvider) authentication.getPrincipal();
        organizationService.newOrganizationRequest(request, userProvider);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{id}/status")
    public ResponseEntity<?> updateOrganizationRequestStatus(
            @PathVariable("id") Long id,
            @RequestBody UpdateOrganizationRequestStatusRequest request
    ) {
        organizationService.updateOrganizationStatus(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{id}/comments")
    public ResponseEntity<?> getOrganizationRequestComments(
            @PathVariable("id") Long id,
            @RequestBody PageableRequest<Comment> request
    ) {
        var organizationRequestComments = organizationService.getOrganizationRequestComments(id, request);
        return PageableResponse.toResponseEntity(organizationRequestComments, request);
    }

    @PostMapping("/requests/{id}/comments/new")
    public ResponseEntity<?> addCommentToOrganizationRequest(
            @PathVariable("id") Long id,
            @RequestBody AddCommentToOrganizationRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    // ******** end region ********
}
