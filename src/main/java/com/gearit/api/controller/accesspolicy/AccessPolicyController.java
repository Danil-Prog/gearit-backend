package com.gearit.api.controller.accesspolicy;


import com.gearit.api.dto.request.CreateAccessPolicyRequest;
import com.gearit.api.dto.response.GetAvailableResourcesResponse;
import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.enpoint.Endpoint;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import com.gearit.api.utils.http.PageableRequest;
import com.gearit.api.utils.http.PageableResponse;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/access-policy")
public class AccessPolicyController {

    private final AccessPolicyService accessPolicyService;

    @Autowired
    public AccessPolicyController(AccessPolicyService accessPolicyService) {
        this.accessPolicyService = accessPolicyService;
    }

    @PostMapping
    public ResponseEntity<PageableResponse<AccessPolicy>> getAccessPolicy(
            @RequestBody PageableRequest<AccessPolicy> request
    ) {
        var response = accessPolicyService.getAllAccessPolicies(request);
        return ResponseEntity.ok(new PageableResponse<>(response.getTotalElements(), response.getContent(), request));
    }

    @GetMapping("/resources")
    public ResponseEntity<GetAvailableResourcesResponse> getAvailableResources(Authentication auth) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        var endpoints = userProvider.getAccessPolicy()
                .getEndpoints()
                .stream()
                .map(Endpoint::getResource)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new GetAvailableResourcesResponse(endpoints));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createAccessPolicy(@RequestBody CreateAccessPolicyRequest request) {
        accessPolicyService.createAccessPolicy(request);
        return ResponseEntity.ok().build();
    }
}
